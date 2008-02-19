/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is GraphMaker. The Initial Developer of the Original
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2006-2007. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: AbstractModel.java 2853 2007-02-25 02:09:25Z nfiedler $
 */

package com.bluemarsh.graphmaker.core.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

/**
 * Class AbstractModel is an abstract implementation of the Model
 * interface. It provides some of the basic behavior of models,
 * including transactions and undoable edit support.
 *
 * @author Nathan Fiedler
 */
public abstract class AbstractModel implements Model {
    /** Handles property change listeners and sending events. */
    private PropertyChangeSupport propSupport;
    /** Handles undoable edit listeners and sending events. */
    private ModelUndoableEditSupport editSupport;
    /** Map of the client properties set in this instance. */
    private Map<Object, Object> propertiesMap;
    /** List of model listeners. */
    private ModelListener modelListeners;
    /** The current open transaction; otherwise null. */
    private Transaction transaction;
    /** Controls the creation of new transactions. */
    private Semaphore transactionSemaphore;

    /**
     * Creates a new instance of AbstractModel.
     */
    public AbstractModel() {
        propSupport = new PropertyChangeSupport(this);
        propertiesMap = new HashMap<Object, Object>();
        editSupport = new ModelUndoableEditSupport(this);
        transactionSemaphore = new Semaphore(1, true);
    }

    public void addModelListener(ModelListener listener) {
        if (listener != null) {
            synchronized (this) {
                modelListeners = ModelEventMulticaster.add(
                        modelListeners, listener);
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propSupport.addPropertyChangeListener(listener);
    }

    public void addUndoableEditListener(UndoableEditListener listener) {
        editSupport.addUndoableEditListener(listener);
    }

    public synchronized void cancelTransaction() {
        if (isInTransaction()) {
            if (!transaction.currentThreadIsTransactionThread()) {
                throw new IllegalStateException("thread is not transaction owner");
            }
            // Roll back the changes made in this transaction.
            if (editSupport.getUpdateLevel() > 0) {
                editSupport.rollback();
            }
            // Once everything is finished, close the transaction.
            transactionSemaphore.release();
            transaction = null;
        }
    }

    /**
     * Ensure that we are in a transaction, and that the requesting
     * thread is the transaction owner.
     */
    protected synchronized void checkInTransaction() {
        if (!isInTransaction()) {
            throw new IllegalStateException("must be in a transaction");
        }
        if (!transaction.currentThreadIsTransactionThread()) {
            throw new IllegalStateException("thread is not transaction owner");
        }
    }

    public synchronized void endTransaction() throws IOException {
        checkInTransaction();
        transaction.fireEvents();
        // Dispatch undoable edit events as a single compound edit.
        // The undoable edit support merges similar edits for us.
        if (editSupport.getUpdateLevel() > 0) {
            editSupport.endUpdate();
        }
        // Once everything is finished, close the transaction.
        transactionSemaphore.release();
        transaction = null;
    }

    /**
     * Fire the given ModelEvent to the registered listeners (when the
     * current transaction is finished). Note that a transaction must
     * be opened when invoking this method.
     *
     * @param  event  ModelEvent to dispatch.
     */
    protected void fireModelEvent(ModelEvent event) {
        checkInTransaction();
        transaction.addModelEvent(event);
    }

    /**
     * Fire the given UndoableEdit to the registered listeners (when the
     * current transaction is finished). Note that a transaction must
     * be opened when invoking this method.
     *
     * @param  edit  UndoableEdit to dispatch.
     */
    protected void fireUndoableEdit(UndoableEdit edit) {
        checkInTransaction();
        transaction.addUndoableEdit(edit);
    }

    public Object getClientProperty(Object key) {
        synchronized (propertiesMap) {
            return propertiesMap.get(key);
        }
    }

    public synchronized boolean isInTransaction() {
        return transaction != null;
    }

    public Set<Object> propertyKeys() {
        return propertiesMap.keySet();
    }

    public void putClientProperty(Object key, Object value) {
        Object oldValue;
        synchronized (propertiesMap) {
            oldValue = propertiesMap.get(key);
            if (value != null) {
                propertiesMap.put(key, value);
            } else if (oldValue != null) {
                propertiesMap.remove(key);
            } else {
                // Nothing changed, do not fire events.
                return;
            }
        }
        propSupport.firePropertyChange(key.toString(), oldValue, value);
    }

    public void removeModelListener(ModelListener listener) {
        if (listener != null) {
            synchronized (this) {
                modelListeners = ModelEventMulticaster.remove(
                        modelListeners, listener);
            }
        }
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propSupport.removePropertyChangeListener(listener);
    }

    public void removeUndoableEditListener(UndoableEditListener listener) {
        editSupport.removeUndoableEditListener(listener);
    }

    public void startTransaction() {
        startTransaction(false);
    }

    /**
     * Begins the transaction for making changes to the model.
     *
     * @param  inUndoRedo  true if undo events should not be collected
     *                     and dispatched to listeners; false if the
     *                     events should be fired as usual.
     */
    private synchronized void startTransaction(boolean inUndoRedo) {
        // Check if this thread is already in a transaction or not.
        if (isInTransaction() && transaction.currentThreadIsTransactionThread()) {
            throw new IllegalStateException("current thread already in a transaction");
        }

        // Force other clients to wait until the current transaction
        // is completed, before starting a new one.
        try {
            transactionSemaphore.acquire();
        } catch (InterruptedException ie) {
            throw new IllegalStateException(
                    "interrupted while acquiring semaphore", ie);
        }
        assert transaction == null;

        // Could check here if the model source is read-only.

        if (!inUndoRedo) {
            transaction = new Transaction(this, editSupport);
            editSupport.beginUpdate();
        } else {
            transaction = new Transaction(this, null);
        }
    }

    /**
     * Represents a set of changes to be made to the model.
     */
    private static class Transaction {
        /** The thread that started this transaction. */
        private Thread transactionThread;
        /** List of model events to be fired when transaction ends. */
        private List<ModelEvent> modelEvents;
        /** Sink for undoable edits, or null to suppress events. */
        private UndoableEditSupport editSupport;
        /** The model that owns this transaction. */
        private AbstractModel model;

        /**
         * Creates a new instance of Transaction.
         *
         * @param  model        the model for which events are dispatched.
         * @param  editSupport  where undoable edits are collected;
         *                      if null, undoable edits are suppressed.
         */
        public Transaction(AbstractModel model,
                UndoableEditSupport editSupport) {
            this.model = model;
            this.editSupport = editSupport;
            transactionThread = Thread.currentThread();
            modelEvents = new ArrayList<ModelEvent>();
        }

        /**
         * Add a ModelEvent to this transaction.
         *
         * @param  event  ModelEvent to add.
         */
        public void addModelEvent(ModelEvent event) {
            modelEvents.add(event);
        }

        /**
         * Add an UndoableEdit to this transaction.
         *
         * @param  edit  UndoableEdit to add.
         */
        public void addUndoableEdit(UndoableEdit edit) {
            if (editSupport != null) {
                editSupport.postEdit(edit);
            }
        }

        /**
         * Determines if currently running thread is the transaction owner.
         *
         * @return  true if thread owns transaction, false otherwise.
         */
        public boolean currentThreadIsTransactionThread() {
            return Thread.currentThread().equals(transactionThread);
        }

        /**
         * Fire the collected events to the listeners registered with the
         * model. Note this does not fire the undoable edits, that is done
         * in the model itself.
         */
        public void fireEvents() {
            ModelListener ml;
            synchronized (model) {
                ml = model.modelListeners;
            }
            if (ml != null) {
                for (ModelEvent event : modelEvents) {
                    event.getType().fireEvent(event, ml);
                }
            }
        }
    }

    /**
     * An UndoableEditSupport that creates a ModelUndoableEdit compound edit.
     */
    private static class ModelUndoableEditSupport extends UndoableEditSupport {
        /** The model on which the edits are applied. */
        private AbstractModel model;

        /**
         * Creates a new instance of ModelUndoableEdit.
         *
         * @param  model  the model on which the edits are applied.
         */
        public ModelUndoableEditSupport(AbstractModel model) {
            super(model);
            this.model = model;
        }

        @Override
        protected CompoundEdit createCompoundEdit() {
            return new ModelUndoableEdit(model);
        }

        /**
         * Roll back the current edits without starting a transaction
         * or dispatching undoable edit events.
         */
        public void rollback() {
            ((ModelUndoableEdit) compoundEdit).rollback();
        }
    }
            
    /**
     * A compound undoable edit that manages the model transaction.
     */
    private static class ModelUndoableEdit extends CompoundEdit {
        /** silence compiler warnings */
        private static final long serialVersionUID = 1L;
        /** The model on which the edits are applied. */
        private AbstractModel model;

        /**
         * Creates a new instance of ModelUndoableEdit.
         *
         * @param  model  the model on which the edits are applied.
         */
        public ModelUndoableEdit(AbstractModel model) {
            super();
            this.model = model;
        }

        @Override
        public void redo() throws CannotRedoException {
            try {
                model.startTransaction(true);
                super.redo();
            } finally {
                try {
                    model.endTransaction();
                } catch (IOException ioe) {
                    CannotRedoException cre = new CannotRedoException();
                    cre.initCause(ioe);
                    throw cre;
                }
            }
        }

        /**
         * Ends the compound edit and calls super.undo() to roll back
         * the edits, without starting a transaction.
         */
        public void rollback() {
            super.end();
            super.undo();
        }

        @Override
        public void undo() throws CannotUndoException {
            try {
                model.startTransaction(true);
                super.undo();
            } finally {
                try {
                    model.endTransaction();
                } catch (IOException ioe) {
                    CannotUndoException cue = new CannotUndoException();
                    cue.initCause(ioe);
                    throw cue;
                }
            }
        }
    }
}

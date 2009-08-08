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
 * are Copyright (C) 2006-2009. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.graphmaker.core.model;

/**
 * Class ModelEventMulticaster implements a thread-safe list of
 * model listeners. It is technically a tree but it grows only
 * in one direction, which makes it more like a linked list. This
 * class behaves like a listener but it simply forwards the events
 * to the contained listeners.
 *
 * <p>This marvelous design was originally put to code by Amy Fowler and
 * John Rose in the form of the <code>AWTEventMulticaster</code> class
 * in the <code>java.awt</code> package. This implementation is based on
 * the description given in <u>Taming Java Threads</u> by Allen Holub.</p>
 */
public class ModelEventMulticaster implements ModelListener {
    /** A session listener. */
    private final ModelListener listener1;
    /** A session listener. */
    private final ModelListener listener2;

    /**
     * Adds the second listener to the first listener and returns the
     * resulting multicast listener.
     *
     * @param  l1  a session listener.
     * @param  l2  the session listener being added.
     * @return  session multicast listener.
     */
    public static ModelListener add(ModelListener l1,
                                         ModelListener l2) {
        return (l1 == null) ? l2 :
               (l2 == null) ? l1 : new ModelEventMulticaster(l1, l2);
    }

    /**
     * Removes the second listener from the first listener and returns
     * the resulting multicast listener.
     *
     * @param  l1  a session listener.
     * @param  l2  the listener being removed.
     * @return  session multicast listener.
     */
    public static ModelListener remove(ModelListener l1,
                                            ModelListener l2) {
        if (l1 == l2 || l1 == null) {
            return null;
        } else if (l1 instanceof ModelEventMulticaster) {
            return ((ModelEventMulticaster) l1).remove(l2);
        } else {
            return l1;
        }
    }

    /**
     * Creates a session event multicaster instance which chains
     * listener l1 with listener l2.
     *
     * @param  l1  a session listener.
     * @param  l2  a session listener.
     */
    protected ModelEventMulticaster(ModelListener l1, ModelListener l2) {
        listener1 = l1;
        listener2 = l2;
    }

    /**
     * Removes a session listener from this multicaster and returns the
     * resulting multicast listener.
     *
     * @param  l  the listener to be removed.
     * @return  the other listener.
     */
    protected ModelListener remove(ModelListener l) {
        if (l == listener1) {
            return listener2;
        }
        if (l == listener2) {
            return listener1;
        }
        // Recursively seek out the target listener.
        ModelListener l1 = remove(listener1, l);
        ModelListener l2 = remove(listener2, l);
        return (l1 == listener1 && l2 == listener2) ? this : add(l1, l2);
    }

    @Override
    public void edgeAdded(ModelEvent event) {
        listener1.edgeAdded(event);
        listener2.edgeAdded(event);
    }

    @Override
    public void edgeRemoved(ModelEvent event) {
        listener1.edgeRemoved(event);
        listener2.edgeRemoved(event);
    }

    @Override
    public void vertexAdded(ModelEvent event) {
        listener1.vertexAdded(event);
        listener2.vertexAdded(event);
    }

    @Override
    public void vertexRemoved(ModelEvent event) {
        listener1.vertexRemoved(event);
        listener2.vertexRemoved(event);
    }
}

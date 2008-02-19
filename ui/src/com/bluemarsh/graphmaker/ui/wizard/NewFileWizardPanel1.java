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
 * $Id: NewFileWizardPanel1.java 2858 2007-02-26 09:05:03Z nfiedler $
 */

package com.bluemarsh.graphmaker.ui.wizard;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.loaders.TemplateWizard;
import org.openide.util.HelpCtx;

/**
 * The first panel of our new GraphMaker document wizard.
 *
 * @author  Nathan Fiedler
 */
public class NewFileWizardPanel1 implements WizardDescriptor.Panel,
        PropertyChangeListener {
    /** The visual component that displays this panel. */
    private NewFileVisualPanel1 component;
    /** Set of change listeners, if any. */
    private Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);
    /** True if the visual panel input is valid. */
    private boolean isValid;
    /** The wizard descriptor. */
    private TemplateWizard wizard;

    /**
     * Creates a new instance of NewFileWizardPanel1.
     *
     * @param  wizard  the wizard descriptor.
     */
    public NewFileWizardPanel1(TemplateWizard wizard) {
        super();
        this.wizard = wizard;
    }

    /**
     * Returns the name of the file to be created (the filename the user
     * chose plus the standard file extension).
     *
     * @return  name of file to be created.
     */
    public String getFilename() {
        if (component != null) {
            return component.getFilename();
        }
        return null;
    }

    public Component getComponent() {
        if (component == null) {
            component = new NewFileVisualPanel1(wizard);
            component.addPropertyChangeListener(
                    NewFileVisualPanel1.PROP_VALID, this);
        }
        return component;
    }

    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    public boolean isValid() {
        return isValid;
    }

    public void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    public void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    /**
     * Notify the change listeners that our state has changed.
     */
    protected void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();
        }
        ChangeEvent ev = new ChangeEvent(this);
        while (it.hasNext()) {
            it.next().stateChanged(ev);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        isValid = ((Boolean) evt.getNewValue()).booleanValue();
        // We are listening only to the 'valid' propety changes.
        fireChangeEvent();
    }

    public void readSettings(Object settings) {
    }

    public void storeSettings(Object settings) {
    }
}

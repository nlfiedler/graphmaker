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
 * are Copyright (C) 2006-2008. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: NewFileWizardIterator.java 2908 2007-03-07 09:53:53Z nfiedler $
 */

package com.bluemarsh.graphmaker.ui.wizard;

import com.bluemarsh.graphmaker.core.model.Model;
import com.bluemarsh.graphmaker.core.model.ModelAccess;
import com.bluemarsh.graphmaker.core.model.ModelFactory;
import com.bluemarsh.graphmaker.core.model.ModelProvider;
import com.bluemarsh.graphmaker.core.model.ModelSource;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Panel;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.TemplateWizard;

/**
 * Iterates the wizard panels for the new GraphMaker document wizard.
 *
 * @author  Nathan Fiedler
 */
public final class NewFileWizardIterator implements TemplateWizard.Iterator {
    /** silence compiler warnings */
    private static final long serialVersionUID = 1L;
    /** Wizard panel offset. */
    private int index;
    /** The wizard descriptor. */
    private TemplateWizard wizard;
    /** The wizard panels. */
    private WizardDescriptor.Panel[] panels;
    /** The wizard panel in which the use selects the new file. */
    private NewFileWizardPanel1 newFilePanel;

    public void initialize(TemplateWizard wizard) {
        this.wizard = wizard;
    }

    public Set<DataObject> instantiate(TemplateWizard wizard) throws IOException {
        String fname = newFilePanel.getFilename();
        File file = new File(fname);
        ModelSource source = ModelSource.create(file, true);
        ModelFactory factory = ModelProvider.getModelFactory();
        ModelAccess access = factory.createAccess(source);
        Model model = factory.createModel();
        access.write(model);
        FileObject fobj = FileUtil.toFileObject(file);
        DataObject dobj = DataObject.find(fobj);
        return dobj == null ? Collections.<DataObject>emptySet() :
            Collections.singleton(dobj);
    }

    public void uninitialize(TemplateWizard wizard) {
        panels = null;
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     *
     * @return  set of wizard panels.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            newFilePanel = new NewFileWizardPanel1(wizard);
            panels = new WizardDescriptor.Panel[] {
                newFilePanel
            };
            String[] steps = new String[panels.length];
            for (int ii = 0; ii < panels.length; ii++) {
                Component c = panels[ii].getComponent();
                steps[ii] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(ii));
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    public Panel current() {
        return getPanels()[index];
    }

    public String name() {
        return index + 1 + ". from " + getPanels().length;
    }

    public boolean hasNext() {
        return index < getPanels().length - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    public void addChangeListener(ChangeListener l) {
    }

    public void removeChangeListener(ChangeListener l) {
    }
}

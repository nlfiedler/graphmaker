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
 * $Id$
 */

package com.bluemarsh.graphmaker.ui;

import com.bluemarsh.graphmaker.core.model.Model;
import com.bluemarsh.graphmaker.core.model.ModelAccess;
import com.bluemarsh.graphmaker.core.model.ModelFactory;
import com.bluemarsh.graphmaker.core.model.ModelProvider;
import com.bluemarsh.graphmaker.core.model.ModelSource;
import java.io.IOException;
import org.openide.ErrorManager;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.PrintCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 * The OpenSupport implementation for GraphMaker data objects.
 *
 * @author Nathan Fiedler
 */
public class GraphOpenSupport extends OpenSupport implements
        CloseCookie, OpenCookie, PrintCookie {
    /** Corresponding data object. */
    private GraphDataObject dataObject;
    /** The graph model, if loaded; null otherwise. */
    private Model model;

    /**
     * Creates a new instance of GraphOpenSupport.
     *
     * @param  dobj  associated data object.
     */
    public GraphOpenSupport(GraphDataObject dobj) {
        super(dobj.getPrimaryEntry());
        dataObject = dobj;
    }

    protected CloneableTopComponent createCloneableTopComponent() {
        return new GraphTopComponent(dataObject);
    }

    /**
     * Returns the environment instance for this open support.
     *
     * @return  open support environment.
     */
    public Env getEnv() {
	return (OpenSupport.Env) env;
    }

    /**
     * Returns an object used for synchronizing access to the model
     * across the multiple cloned editors.
     *
     * @return  object for synchronization.
     */
    protected Object getLock() {
        // CloneableEditorSupport uses this as a lock, so it must be
        // good enough for our purposes.
        return allEditors;
    }

    /**
     * Returns the graph Model instance for the associated editor.
     *
     * @return  graph model.
     */
    public Model getModel() {
        synchronized (getLock()) {
            return model;
        }
    }

    /**
     * Ensures the model is loaded into memory.
     */
    private void loadModel() {
        synchronized (getLock()) {
            if (model == null) {
                // Load the model into memory.
                FileObject fobj = dataObject.getPrimaryFile();
                ModelSource source = ModelSource.create(fobj, fobj.canWrite());
                ModelFactory factory = ModelProvider.getModelFactory();
                ModelAccess access = factory.createAccess(source);
                try {
                    model = access.read();
                } catch (IOException ioe) {
                    ErrorManager.getDefault().notify(ioe);
                    return;
                }
            }
        }
    }

    public void open() {
        loadModel();
        super.open();
    }

    public void print() {
        loadModel();
    }

    /**
     * Saves the model to disk.
     *
     * @throws  IOException
     *          if an error occurs writing to the file.
     */
    public void saveModel() throws IOException {
        synchronized (getLock()) {
            if (model != null) {
                FileObject fobj = dataObject.getPrimaryFile();
                ModelSource source = ModelSource.create(fobj, fobj.canWrite());
                ModelFactory factory = ModelProvider.getModelFactory();
                ModelAccess access = factory.createAccess(source);
                access.write(model);
            }
        }
    }
}

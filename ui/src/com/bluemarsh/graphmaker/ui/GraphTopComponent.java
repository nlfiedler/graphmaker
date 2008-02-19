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
 * $Id: GraphTopComponent.java 2907 2007-03-07 09:42:43Z nfiedler $
 */

package com.bluemarsh.graphmaker.ui;

import com.bluemarsh.graphmaker.ui.editor.GraphPanel;
import java.awt.BorderLayout;
import org.openide.awt.UndoRedo;
import org.openide.util.NbBundle;
import org.openide.windows.CloneableTopComponent;

/**
 * Class GraphTopComponent contains the user interface for the graph
 * document editor. Essentially it is a container for the graphical
 * display of the model data, and provides tools for modifying the
 * structure and properties of the model components.
 *
 * @author Nathan Fiedler
 */
public class GraphTopComponent extends CloneableTopComponent {
    /** silence compiler warnings */
    private static final long serialVersionUID = 1L;
    /** Corresponding data object. */
    private GraphDataObject dataObject;
    /** The component that contains the graph widgets. */
    private GraphPanel graphPanel;

    /**
     * Creates a new instance of GraphTopComponent.
     *
     * @param  dobj  associated data object.
     */
    public GraphTopComponent(GraphDataObject dobj) {
        super();
        dataObject = dobj;
        setLayout(new BorderLayout());
    }

    protected void componentClosed() {
        super.componentClosed();
        removeAll();
        graphPanel = null;
    }

    protected void componentOpened() {
        super.componentOpened();
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.CENTER);
    }

    public String getDisplayName() {
        return dataObject.getName();
    }

    public int getPersistenceType() {
        return PERSISTENCE_ONLY_OPENED;
    }

    public String getToolTipText() {
        return NbBundle.getMessage(GraphTopComponent.class,
                "HINT_GraphTopComponent");
    }

    public UndoRedo getUndoRedo() {
        return super.getUndoRedo();
    }

    protected String preferredID() {
        return dataObject.getName();
    }
}

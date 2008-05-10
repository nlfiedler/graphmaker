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

package com.bluemarsh.graphmaker.core.model;

import java.util.LinkedList;
import java.util.List;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.openide.util.NbBundle;

/**
 * An EdgeAddUndoableEdit handles the addition of edges to a model.
 *
 * @author Nathan Fiedler
 */
public class EdgeAddUndoableEdit extends AbstractUndoableEdit {
    /** silence compiler warnings */
    private static final long serialVersionUID = 1L;
    /** The model on which to perform the operations. */
    private Model model;
    /** List of affected edges. */
    private List<Edge> edges;

    /**
     * Creates a new instance of EdgeAddUndoableEdit.
     *
     * @param  model  the Model on which to operate.
     * @param  edge   the Edge involved in the edit.
     */
    public EdgeAddUndoableEdit(Model model, Edge edge) {
        super();
        this.model = model;
        edges = new LinkedList<Edge>();
        edges.add(edge);
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        if (anEdit instanceof EdgeAddUndoableEdit) {
            EdgeAddUndoableEdit ue = (EdgeAddUndoableEdit) anEdit;
            if (model.equals(ue.model)) {
                edges.addAll(ue.edges);
                ue.die();
                return true;
            }
        }
        return false;
    }

    @Override
    public void die() {
	super.die();
        edges.clear();
    }

    @Override
    public String getPresentationName() {
        if (edges.size() > 1) {
            return NbBundle.getMessage(EdgeAddUndoableEdit.class,
                    "LBL_EdgeAddUndoableEdit_AddEdges");
        } else {
            return NbBundle.getMessage(EdgeAddUndoableEdit.class,
                    "LBL_EdgeAddUndoableEdit_AddEdge");
        }
    }

    @Override
    public String getRedoPresentationName() {
        if (edges.size() > 1) {
            return NbBundle.getMessage(EdgeAddUndoableEdit.class,
                    "LBL_EdgeAddUndoableEdit_RedoAddEdges");
        } else {
            return NbBundle.getMessage(EdgeAddUndoableEdit.class,
                    "LBL_EdgeAddUndoableEdit_RedoAddEdge");
        }
    }

    @Override
    public String getUndoPresentationName() {
        if (edges.size() > 1) {
            return NbBundle.getMessage(EdgeAddUndoableEdit.class,
                    "LBL_EdgeAddUndoableEdit_UndoAddEdges");
        } else {
            return NbBundle.getMessage(EdgeAddUndoableEdit.class,
                    "LBL_EdgeAddUndoableEdit_UndoAddEdge");
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        for (Edge edge : edges) {
            model.addEdge(edge);
        }
        super.redo();
    }

    @Override
    public void undo() throws CannotUndoException {
        for (Edge edge : edges) {
            model.removeEdge(edge);
        }
        super.undo();
    }
}

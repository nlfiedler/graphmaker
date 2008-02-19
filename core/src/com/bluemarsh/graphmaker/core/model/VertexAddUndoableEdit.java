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
 * $Id: VertexAddUndoableEdit.java 2853 2007-02-25 02:09:25Z nfiedler $
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
 * An VertexAddUndoableEdit handles the addition of vertices to a model.
 *
 * @author Nathan Fiedler
 */
public class VertexAddUndoableEdit extends AbstractUndoableEdit {
    /** silence compiler warnings */
    private static final long serialVersionUID = 1L;
    /** The model on which to perform the operations. */
    private Model model;
    /** List of affected vertices. */
    private List<Vertex> vertices;

    /**
     * Creates a new instance of VertexAddUndoableEdit.
     *
     * @param  model   the Model on which to operate.
     * @param  vertex  the Vertex involved in the edit.
     */
    public VertexAddUndoableEdit(Model model, Vertex vertex) {
        super();
        this.model = model;
        vertices = new LinkedList<Vertex>();
        vertices.add(vertex);
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        if (anEdit instanceof VertexAddUndoableEdit) {
            VertexAddUndoableEdit ue = (VertexAddUndoableEdit) anEdit;
            if (model.equals(ue.model)) {
                vertices.addAll(ue.vertices);
                ue.die();
                return true;
            }
        }
        return false;
    }

    @Override
    public void die() {
	super.die();
        vertices.clear();
    }

    @Override
    public String getPresentationName() {
        if (vertices.size() > 1) {
            return NbBundle.getMessage(VertexAddUndoableEdit.class,
                    "LBL_VertexAddUndoableEdit_AddVertices");
        } else {
            return NbBundle.getMessage(VertexAddUndoableEdit.class,
                    "LBL_VertexAddUndoableEdit_AddVertex");
        }
    }

    @Override
    public String getRedoPresentationName() {
        if (vertices.size() > 1) {
            return NbBundle.getMessage(VertexAddUndoableEdit.class,
                    "LBL_VertexAddUndoableEdit_RedoAddVertices");
        } else {
            return NbBundle.getMessage(VertexAddUndoableEdit.class,
                    "LBL_VertexAddUndoableEdit_RedoAddVertex");
        }
    }

    @Override
    public String getUndoPresentationName() {
        if (vertices.size() > 1) {
            return NbBundle.getMessage(VertexAddUndoableEdit.class,
                    "LBL_VertexAddUndoableEdit_UndoAddVertices");
        } else {
            return NbBundle.getMessage(VertexAddUndoableEdit.class,
                    "LBL_VertexAddUndoableEdit_UndoAddVertex");
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        for (Vertex vertex : vertices) {
            model.addVertex(vertex);
        }
        super.redo();
    }

    @Override
    public void undo() throws CannotUndoException {
        for (Vertex vertex : vertices) {
            model.removeVertex(vertex);
        }
        super.undo();
    }
}

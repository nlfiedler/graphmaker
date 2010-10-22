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
 * are Copyright (C) 2006-2010. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */
package com.bluemarsh.graphmaker.core.model;

import java.io.IOException;
import javax.swing.undo.UndoManager;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the model concrete implementation via the Model API.
 *
 * @author  Nathan Fiedler
 */
public class ModelTest {

    @Test
    public void test_Model() {
        ModelFactory factory = ModelProvider.getModelFactory();
        Model model = factory.createModel();

        // Test basic transaction support.
        assertFalse(model.isInTransaction());
        model.startTransaction();
        assertTrue(model.isInTransaction());
        try {
            model.endTransaction();
        } catch (IOException ioe) {
            fail(ioe.toString());
        }
        assertFalse(model.isInTransaction());

        // Test basic client property support.
        Object value = model.getClientProperty("key");
        assertNull(value);
        model.putClientProperty("key", new Object());
        value = model.getClientProperty("key");
        assertNotNull(value);
        model.putClientProperty("key", null);
        value = model.getClientProperty("key");
        assertNull(value);

        // Additional transaction testing.
        try {
            Vertex v1 = factory.createVertex(1, 1, 1, 1.0d);
            Vertex v2 = factory.createVertex(1, 1, 1, 1.0d);
            model.addEdge(factory.createEdge(v1, v2, true));
            fail("requires a transaction, but succeeded anyway");
        } catch (IllegalStateException ise) {
            // This is expected and okay
        }
        try {
            model.addVertex(factory.createVertex(1, 1, 1, 1.0d));
            fail("requires a transaction, but succeeded anyway");
        } catch (IllegalStateException ise) {
            // This is expected and okay
        }
        try {
            Vertex v1 = factory.createVertex(1, 1, 1, 1.0d);
            Vertex v2 = factory.createVertex(2, 2, 2, 1.0d);
            model.removeEdge(factory.createEdge(v1, v2, true));
            fail("requires a transaction, but succeeded anyway");
        } catch (IllegalStateException ise) {
            // This is expected and okay
        }
        try {
            model.removeVertex(factory.createVertex(1, 1, 1, 1.0d));
            fail("requires a transaction, but succeeded anyway");
        } catch (IllegalStateException ise) {
            // This is expected and okay
        }

        // Test adding and removing elements.
        model.startTransaction();
        assertTrue(model.getVertices().isEmpty());
        assertTrue(model.getEdges().isEmpty());
        Vertex v1 = factory.createVertex(1, 1, 1, 1.0d);
        model.addVertex(v1);
        Vertex v2 = factory.createVertex(2, 2, 2, 1.0d);
        model.addVertex(v2);
        assertTrue(model.getVertices().size() == 2);
        Edge e1 = factory.createEdge(v1, v2, true);
        model.addEdge(e1);
        assertTrue(model.getEdges().size() == 1);
        model.removeEdge(e1);
        assertTrue(model.getEdges().isEmpty());
        model.removeVertex(v1);
        model.removeVertex(v2);
        assertTrue(model.getVertices().isEmpty());
        try {
            model.endTransaction();
        } catch (IOException ioe) {
            fail(ioe.toString());
        }

        // Test cancelling an open transaction and the rollback.
        UndoManager undo = new UndoManager();
        model.addUndoableEditListener(undo);
        model.startTransaction();
        assertTrue(!undo.canUndo());
        assertTrue(!undo.canRedo());
        assertTrue(model.getVertices().isEmpty());
        assertTrue(model.getEdges().isEmpty());
        v1 = factory.createVertex(1, 1, 1, 1.0d);
        model.addVertex(v1);
        v2 = factory.createVertex(2, 2, 2, 1.0d);
        model.addVertex(v2);
        assertTrue(model.getVertices().size() == 2);
        e1 = factory.createEdge(v1, v2, true);
        model.addEdge(e1);
        assertTrue(model.getEdges().size() == 1);
        assertTrue(!undo.canUndo());
        assertTrue(!undo.canRedo());
        model.cancelTransaction();
        assertTrue(!undo.canUndo());
        assertTrue(!undo.canRedo());
        assertTrue(model.getEdges().isEmpty());
        assertTrue(model.getVertices().isEmpty());

        // Test undo/redo support (starting with a fresh model).
        undo = new UndoManager();
        model = factory.createModel();
        model.addUndoableEditListener(undo);
        assertTrue(model.getVertices().isEmpty());
        assertTrue(model.getEdges().isEmpty());
        model.startTransaction();
        v1 = factory.createVertex(10, 10, 10, 1.0d);
        model.addVertex(v1);
        v2 = factory.createVertex(20, 20, 20, 1.0d);
        model.addVertex(v2);
        try {
            model.endTransaction();
        } catch (IOException ioe) {
            fail(ioe.toString());
        }
        // Start a second transaction for adding the edge.
        model.startTransaction();
        e1 = factory.createEdge(v1, v2, true);
        model.addEdge(e1);
        try {
            model.endTransaction();
        } catch (IOException ioe) {
            fail(ioe.toString());
        }
        assertTrue(model.getVertices().size() == 2);
        assertTrue(model.getEdges().size() == 1);
        undo.undo();
        assertTrue(model.getEdges().isEmpty());
        assertTrue(model.getVertices().size() == 2);
        undo.undo();
        assertTrue(model.getVertices().isEmpty());
        assertTrue(model.getEdges().isEmpty());
        assertTrue(!undo.canUndo());
        undo.redo();
        assertTrue(model.getEdges().isEmpty());
        assertTrue(model.getVertices().size() == 2);
        undo.redo();
        assertTrue(model.getVertices().size() == 2);
        assertTrue(model.getEdges().size() == 1);
        assertTrue(!undo.canRedo());
    }
}

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

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the model access concrete implementation via the ModelAccess API.
 *
 * @author  Nathan Fiedler
 */
public class ModelAccessTest {

    private void deleteFile(File file) {
        if (file != null) {
            file.delete();
        }
    }

    @Test
    public void test_ModelAccess() {
        ModelFactory factory = ModelProvider.getModelFactory();
        Model model = factory.createModel();

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
        try {
            model.endTransaction();
        } catch (IOException ioe) {
            fail(ioe.toString());
        }

        ModelAccess access = null;
        File file = null;
        try {
            file = File.createTempFile("graphmaker-unit", ".gmx");
            ModelSource source = ModelSource.create(file, true);
            access = factory.createAccess(source);
        } catch (IOException ioe) {
            deleteFile(file);
            fail(ioe.toString());
        }
        try {
            access.write(model);
        } catch (IOException ioe) {
            deleteFile(file);
            fail("access write failed: " + ioe.toString());
        }
        Model model2 = null;
        try {
            model2 = access.read();
        } catch (IOException ioe) {
            deleteFile(file);
            fail("access read failed: " + ioe.toString());
        }
        assertNotNull(model2);
        assertTrue(model2.getVertices().size() == 2);
        assertTrue(model2.getEdges().size() == 1);
        deleteFile(file);
    }
}

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

/**
 * A ModelFactory creates instances of the Model class, as well as
 * vertices and edges. A concrete implementation can be acquired from
 * the <code>ModelProvider</code> class.
 *
 * @author Nathan Fiedler
 */
public interface ModelFactory {

    /**
     * Creates a ModelAccess appropriate for the given ModelSource.
     *
     * @param  source  ModelSource, provides file to back the model.
     * @return  access object for reading and writing the model.
     */
    ModelAccess createAccess(ModelSource source);

    /**
     * Creates an Edge instance with the given set of attributes.
     *
     * @param  source    starting endpoint of edge.
     * @param  target    ending endpoint of edge.
     * @param  directed  true if edge should be directed.
     * @return  new edge.
     */
    Edge createEdge(Vertex source, Vertex target, boolean directed);

    /**
     * Creates an empty Model instance.
     *
     * @return  new model.
     */
    Model createModel();

    /**
     * Creates a Vertex instance with the given set of attributes.
     *
     * @param  x     x position.
     * @param  y     y position.
     * @param  z     z position.
     * @param  cost  cost value.
     * @return  new vertex.
     */
    Vertex createVertex(int x, int y, int z, double cost);
}

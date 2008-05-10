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
 * are Copyright (C) 2007. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.graphmaker.ui.nodes;

import com.bluemarsh.graphmaker.core.model.Component;
import com.bluemarsh.graphmaker.core.model.Edge;
import com.bluemarsh.graphmaker.core.model.Vertex;
import org.openide.nodes.Node;

/**
 * A factory to create Nodes to represent various components.
 *
 * @author Nathan Fiedler
 */
public abstract class NodeFactory {
    /** The default instance of the NodeFactory. */
    private static NodeFactory defaultFactory;

    /**
     * Retrieves the default implementation of the NodeFactory.
     *
     * @return  node factory implementation.
     */
    public static synchronized NodeFactory getDefault() {
        if (defaultFactory == null) {
            defaultFactory = new DefaultNodeFactory();
        }
        return defaultFactory;
    }

    /**
     * Creates a Node to represent the component.
     *
     * @param  component  the model component.
     * @return  node for the component.
     */
    public abstract Node create(Component component);

    /**
     * Creates a Node to represent the given Edge component.
     *
     * @param  edge  model edge to be represented.
     * @return  node for the edge.
     */
    public abstract EdgeNode createEdgeNode(Edge edge);

    /**
     * Creates a Node to represent the given Vertex component.
     *
     * @param  vertex  model vertex to be represented.
     * @return  node for the vertex.
     */
    public abstract VertexNode createVertexNode(Vertex vertex);

    /**
     * Default implementation of NodeFactory.
     */
    private static class DefaultNodeFactory extends NodeFactory {

        public Node create(Component component) {
            if (component instanceof Edge) {
                return createEdgeNode((Edge) component);
            } else if (component instanceof Vertex) {
                return createVertexNode((Vertex) component);
            }
            return null;
        }

        @Override
        public EdgeNode createEdgeNode(Edge edge) {
            return new DefaultEdgeNode(edge);
        }

        @Override
        public VertexNode createVertexNode(Vertex vertex) {
            return new DefaultVertexNode(vertex);
        }
    }
}

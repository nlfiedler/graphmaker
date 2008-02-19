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
 * $Id: VertexNode.java 2868 2007-03-02 07:57:35Z nfiedler $
 */

package com.bluemarsh.graphmaker.ui.nodes;

import com.bluemarsh.graphmaker.core.model.Vertex;

/**
 * An VertexNode represents an Vertex in the graph model.
 *
 * @author  Nathan Fiedler
 */
public abstract class VertexNode extends ComponentNode<Vertex> {

    /**
     * Creates a new instance of VertexNode.
     *
     * @param  vertex  the vertex component.
     */
    public VertexNode(Vertex vertex) {
        super(vertex);
    }
}

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

package com.bluemarsh.graphmaker.ui.editor;

import com.bluemarsh.graphmaker.core.model.Edge;
import com.bluemarsh.graphmaker.core.model.Vertex;
import com.bluemarsh.graphmaker.ui.widgets.WidgetFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.Widget;

/**
 * Implements the scene in which the graph components are displayed.
 *
 * @author  Nathan Fiedler
 */
public class GraphMakerScene extends GraphScene<Vertex, Edge> {

    /**
     * Creates a new instance of GraphMakerScene.
     */
    public GraphMakerScene() {
    }

    protected Widget attachNodeWidget(Vertex node) {
        Widget widget = WidgetFactory.getDefault().createWidget(this, node);
        addChild(widget);
        return widget;
    }

    protected Widget attachEdgeWidget(Edge edge) {
        Widget widget = WidgetFactory.getDefault().createWidget(this, edge);
        addChild(widget);
        return widget;
    }

    protected void attachEdgeSourceAnchor(Edge edge, Vertex oldSource, Vertex source) {
        throw new UnsupportedOperationException("Not supported yet.");
        // Widget sourceNodeWidget = findWidget(source);
        // Anchor sourceAnchor = AnchorFactory.createRectangularAnchor(sourceNodeWidget)
        // ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        // edgeWidget.setSourceAnchor(sourceAnchor);
    }

    protected void attachEdgeTargetAnchor(Edge arg0, Vertex oldTarget, Vertex target) {
        throw new UnsupportedOperationException("Not supported yet.");
        // Widget targetNodeWidget = findWidget(target);
        // Anchor targetAnchor = AnchorFactory.createRectangularAnchor(targetNodeWidget)
        // ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        // edgeWidget.setTargetAnchor(targetAnchor);
    }
}

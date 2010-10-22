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
 * are Copyright (C) 1997-2010. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */
package com.bluemarsh.graphmaker.core.model;

/**
 * An Edge represents the connection between two vertices. It can be
 * directed, which implies that traversal can only be made in one
 * direction (from starting to ending vertex).
 *
 * @author Nathan Fiedler
 */
public interface Edge extends Component {

    /** Property name. */
    public static final String PROP_DIRECTED = "directed";
    /** Property name. */
    public static final String PROP_SOURCE = "source";
    /** Property name. */
    public static final String PROP_TARGET = "target";

    /**
     * Computes the angle, in radians, of the edge from the positive
     * x-axis, where the source vertex marks the origin of the edge. The
     * angle will be in the range of 0 to 2<em>pi</em> and will increase
     * counter-clockwise around the origin.
     *
     * @return  angle in radians from positive x-axis.
     */
    double computeAngle();

    /**
     * Computes the distance from the source vertex to the target vertex.
     *
     * @return  computed length of the edge.
     */
    double computeLength();

    /**
     * Get the vertex that starts this edge.
     *
     * @return  starting vertex of this edge.
     */
    Vertex getSource();

    /**
     * Get the vertex that ends this edge.
     *
     * @return  ending vertex of this edge.
     */
    Vertex getTarget();

    /**
     * Get the current directed state of this edge.
     *
     * @return  true if vertex is directed.
     */
    boolean isDirected();

    /**
     * Set the directed state of this edge. If the edge is stored in a
     * larger data structure, you should use any method that data structure
     * provides for making an edge directed. Otherwise you run the risk of
     * corrupting that data structure by making an edge directed.
     *
     * @param  directed  true to make edge directed, false otherwise.
     */
    void setDirected(boolean directed);

    /**
     * Set the vertex that starts this edge.
     *
     * @param  source  starting vertex.
     */
    void setSource(Vertex source);

    /**
     * Set the vertex that ends this edge.
     *
     * @param  target  ending vertex.
     */
    void setTarget(Vertex target);
}

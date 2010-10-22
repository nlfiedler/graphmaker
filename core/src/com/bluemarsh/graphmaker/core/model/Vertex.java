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
 * are Copyright (C) 1999-2010. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */
package com.bluemarsh.graphmaker.core.model;

/**
 * A Vertex represents a point on the graph. It encapsulates the position,
 * size, and cost of a vertex.
 *
 * @author Nathan Fiedler
 */
public interface Vertex extends Component {

    /** Property name. */
    public static final String PROP_DEPTH = "depth";
    /** Property name. */
    public static final String PROP_HEIGHT = "height";
    /** Property name. */
    public static final String PROP_WIDTH = "width";
    /** Property name. */
    public static final String PROP_X = "x";
    /** Property name. */
    public static final String PROP_Y = "y";
    /** Property name. */
    public static final String PROP_Z = "z";

    /**
     * Computes the distance between this vertex and the one given.
     *
     * @param  v  other vertex.
     * @return  distance between the vertices.
     */
    double computeDistance(Vertex v);

    /**
     * Determines if the given x,y,z point falls within the vertex or not.
     * Currently this method ignores the z coordinate and only considers
     * the x and y coordinates, using the  vertex width and height to
     * define a two-dimensional ellipse.
     *
     * @param  x  x coordinate.
     * @param  y  y coordinate.
     * @param  z  z coordinate.
     * @return  true if point inside vertex, false otherwise.
     */
    boolean contains(int x, int y, int z);

    /**
     * Returns the depth of this vertex.
     *
     * @return  depth of vertex
     */
    int getDepth();

    /**
     * Returns the height of this vertex.
     *
     * @return  height of vertex
     */
    int getHeight();

    /**
     * Returns the width of this vertex.
     *
     * @return  width of vertex
     */
    int getWidth();

    /**
     * Retrieve the x coordinate of the vertex.
     *
     * @return  x position of vertex
     */
    int getX();

    /**
     * Retrieve the y coordinate of the vertex.
     *
     * @return  y position of vertex
     */
    int getY();

    /**
     * Retrieve the z coordinate of the vertex.
     *
     * @return  z position of vertex
     */
    int getZ();

    /**
     * Sets the depth of this vertex. It only sets the depth if
     * the given value is greater than zero.
     *
     * @param  depth  new depth for this vertex
     */
    void setDepth(int depth);

    /**
     * Sets the height of this vertex. It only sets the height if
     * the given value is greater than zero.
     *
     * @param  height  new height for this vertex
     */
    void setHeight(int height);

    /**
     * Sets the width of this vertex. It only sets the width if
     * the given value is greater than zero.
     *
     * @param  width  new width for this vertex
     */
    void setWidth(int width);

    /**
     * Set the x coordinate of the vertex.
     *
     * @param  x  x position of vertex
     */
    void setX(int x);

    /**
     * Set the y coordinate of the vertex.
     *
     * @param  y  y position of vertex
     */
    void setY(int y);

    /**
     * Set the z coordinate of the vertex.
     *
     * @param  z  z position of vertex
     */
    void setZ(int z);
}

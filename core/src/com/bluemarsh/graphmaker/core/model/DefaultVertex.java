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
 * are Copyright (C) 1999-2007. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.graphmaker.core.model;

/**
 * Default implementation of the Vertex interface.
 *
 * @author  Nathan Fiedler
 */
public class DefaultVertex extends AbstractComponent implements Vertex {
    /** Position of the center of this vertex on the x axis. */
    private int x;
    /** Position of the center of this vertex on the y axis. */
    private int y;
    /** Position of the center of this vertex on the z axis. */
    private int z;
    /** Width of the bounding box of this vertex. */
    private int width;
    /** Height of the bounding box of this vertex. */
    private int height;
    /** Depth of the bounding box of this vertex. */
    private int depth;

    /**
     * Creates a new instance of DefaultVertex.
     */
    public DefaultVertex() {
    }

    public double computeDistance(Vertex v) {
	return Math.sqrt(Math.pow(x - v.getX(), 2) +
                         Math.pow(y - v.getY(), 2) +
                         Math.pow(z - v.getZ(), 2));
    }

    public boolean contains(int x, int y, int z) {
        int nx = x - this.x;
        int ny = y - this.y;
        int a = width / 2;
        int b = height / 2;
        return ((nx * nx) / (a * a) + (ny * ny) / (b * b)) <= 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Vertex) {
	    Vertex v = (Vertex) obj;
	    if (v.getX() == x && v.getY() == y && v.getZ() == z) {
		return true;
	    }
	}
        return false;
    }

    public int getDepth() {
	return depth;
    }

    public int getHeight() {
	return height;
    }

    public int getWidth() {
	return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int hashCode() {
        return (x << 24) + (y << 12) + z;
    }

    public void setDepth(int depth) {
        int old = this.depth;
        if (depth > 0) {
            this.depth = depth;
        }
        propSupport.firePropertyChange(PROP_DEPTH, old, depth);
    }

    public void setHeight(int height) {
        int old = this.height;
        if (height > 0) {
            this.height = height;
        }
        propSupport.firePropertyChange(PROP_HEIGHT, old, height);
    }

    public void setWidth(int width) {
        int old = this.width;
        if (width > 0) {
            this.width = width;
        }
        propSupport.firePropertyChange(PROP_WIDTH, old, width);
    }

    public void setX(int x) {
        int old = this.x;
        this.x = x;
        propSupport.firePropertyChange(PROP_X, old, x);
    }

    public void setY(int y) {
        int old = this.y;
        this.y = y;
        propSupport.firePropertyChange(PROP_Y, old, y);
    }

    public void setZ(int z) {
        int old = this.z;
        this.z = z;
        propSupport.firePropertyChange(PROP_Z, old, z);
    }
}

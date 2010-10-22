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
 * Default implementation of the Edge interface.
 *
 * @author  Nathan Fiedler
 */
public class DefaultEdge extends AbstractComponent implements Edge {

    /** Vertex that begins this edge. */
    private Vertex source;
    /** Vertex that ends this edge. */
    private Vertex target;
    /** True if this edge is directed. */
    private boolean directed;

    /**
     * Creates a new instance of DefaultEdge.
     */
    public DefaultEdge() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof DefaultEdge) {
            DefaultEdge e = (DefaultEdge) obj;
            if (e.getTarget() == getTarget() && e.getSource() == getSource()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double computeAngle() {
        // Subtract y coordinates in reverse order from normal
        // as the screen coordinate system is flipped along the
        // x-axis from the Cartesian coordinate system.
        double angle = Math.atan2(source.getY() - target.getY(),
                target.getX() - source.getX());
        if (angle < 0) {
            // atan2 returns angle in phase -pi to pi, which means
            // we have to convert the answer into 0 to 2pi range.
            angle += 2 * Math.PI;
        }
        return angle;
    }

    @Override
    public double computeLength() {
        return source.computeDistance(target);
    }

    @Override
    public Vertex getSource() {
        return source;
    }

    @Override
    public Vertex getTarget() {
        return target;
    }

    @Override
    public int hashCode() {
        return (source.hashCode() << 8) + target.hashCode() + (directed ? 0 : 1);
    }

    @Override
    public boolean isDirected() {
        return directed;
    }

    @Override
    public void setDirected(boolean directed) {
        boolean old = this.directed;
        this.directed = directed;
        propSupport.firePropertyChange(PROP_DIRECTED, old, directed);
    }

    @Override
    public void setSource(Vertex source) {
        Vertex old = this.source;
        this.source = source;
        propSupport.firePropertyChange(PROP_SOURCE, old, source);
    }

    @Override
    public void setTarget(Vertex target) {
        Vertex old = this.target;
        this.target = target;
        propSupport.firePropertyChange(PROP_TARGET, old, target);
    }
}

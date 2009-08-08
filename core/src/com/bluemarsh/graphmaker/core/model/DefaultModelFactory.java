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
 * are Copyright (C) 2006-2009. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.graphmaker.core.model;

/**
 * DefaultModelFactory creates instances of model classes using the default
 * implementations in the <code>com.bluemarsh.graphmaker.core.model</code>
 * package.
 *
 * @author Nathan Fiedler
 */
public class DefaultModelFactory implements ModelFactory {

    @Override
    public ModelAccess createAccess(ModelSource source) {
        ModelAccess ma = new DefaultModelAccess();
        ma.setSource(source);
        return ma;
    }

    @Override
    public Edge createEdge(Vertex source, Vertex target, boolean directed) {
        Edge e = new DefaultEdge();
        e.setSource(source);
        e.setTarget(target);
        e.setDirected(directed);
        return e;
    }

    @Override
    public Model createModel() {
        return new DefaultModel();
    }

    @Override
    public Vertex createVertex(int x, int y, int z, double cost) {
        Vertex v = new DefaultVertex();
        v.setX(x);
        v.setY(y);
        v.setZ(z);
        v.setCost(cost);
        return v;
    }
}

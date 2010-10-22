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
 * $Id: ModelEvent.java 31 2009-08-08 14:08:44Z nathanfiedler $
 */
package com.bluemarsh.graphmaker.core.model;

/**
 * Type of model event.
 */
public enum ModelEventType {

    /** Edge was added to the model. */
    EDGE_ADDED {

        @Override
        public void fireEvent(ModelEvent e, ModelListener l) {
            l.edgeAdded(e);
        }
    }, /** Edge was removed from the model. */
    EDGE_REMOVED {

        @Override
        public void fireEvent(ModelEvent e, ModelListener l) {
            l.edgeRemoved(e);
        }
    }, /** Vertex was added to the model. */
    VERTEX_ADDED {

        @Override
        public void fireEvent(ModelEvent e, ModelListener l) {
            l.vertexAdded(e);
        }
    }, /** Vertex was removed from the model. */
    VERTEX_REMOVED {

        @Override
        public void fireEvent(ModelEvent e, ModelListener l) {
            l.vertexRemoved(e);
        }
    };

    /**
     * Dispatches the event to the listener.
     *
     * @param  e  event to dispatch.
     * @param  l  listener to receive event.
     */
    public abstract void fireEvent(ModelEvent e, ModelListener l);
}

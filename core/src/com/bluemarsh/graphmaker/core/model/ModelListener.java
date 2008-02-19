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
 * $Id: ModelListener.java 2853 2007-02-25 02:09:25Z nfiedler $
 */

package com.bluemarsh.graphmaker.core.model;

import java.util.EventListener;

/**
 * The listener interface for receiving events related to models.
 *
 * @author  Nathan Fiedler
 */
public interface ModelListener extends EventListener {

    /**
     * Invoked when a edge has been added to the model.
     *
     * @param  event  model event.
     */
    void edgeAdded(ModelEvent event);

    /**
     * Invoked when a edge has been removed from the model.
     *
     * @param  event  model event.
     */
    void edgeRemoved(ModelEvent event);

    /**
     * Invoked when a vertex has been added to the model.
     *
     * @param  event  model event.
     */
    void vertexAdded(ModelEvent event);

    /**
     * Invoked when a vertex has been removed from the model.
     *
     * @param  event  model event.
     */
    void vertexRemoved(ModelEvent event);
}

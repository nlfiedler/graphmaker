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
 * $Id$
 */
package com.bluemarsh.graphmaker.core.model;

import java.util.EventObject;

/**
 * An event which indicates that a model has changed.
 *
 * @author  Nathan Fiedler
 */
public class ModelEvent extends EventObject {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;
    /** The model that changed. */
    private transient Model model;
    /** The type of model change. */
    private ModelEventType type;

    /**
     * Creates a new instance of ModelEvent.
     *
     * @param  model  model that changed (source of event).
     * @param  type   type of model change.
     */
    public ModelEvent(Model model, ModelEventType type) {
        super(model);
        this.model = model;
        this.type = type;
    }

    /**
     * Get the model that changed.
     *
     * @return  model.
     */
    public Model getModel() {
        return model;
    }

    /**
     * Get the model event type.
     *
     * @return  model event type.
     */
    public ModelEventType getType() {
        return type;
    }
}

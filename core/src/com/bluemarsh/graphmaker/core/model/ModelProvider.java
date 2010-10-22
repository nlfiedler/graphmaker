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

import org.openide.util.Lookup;

/**
 * Class ModelProvider provides easy access to important model classes.
 *
 * @author Nathan Fiedler
 */
public class ModelProvider {

    /** The ModelFactory instance, if it has already been retrieved. */
    private static ModelFactory modelFactory;

    /**
     * Creates a new instance of ModelProvider.
     */
    private ModelProvider() {
    }

    /**
     * Retrieve the ModelFactory instance, creating one if necessary.
     *
     * @return  ModelFactory instance.
     */
    public static synchronized ModelFactory getModelFactory() {
        if (modelFactory == null) {
            // Perform lookup to find a ModelFactory instance.
            modelFactory = Lookup.getDefault().lookup(ModelFactory.class);
        }
        return modelFactory;
    }
}

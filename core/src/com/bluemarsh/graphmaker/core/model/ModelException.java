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

/**
 * An exception that occurred in the Model.
 *
 * @author Nathan Fiedler
 */
public class ModelException extends Exception {

    /** silence compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of ModelException.
     */
    public ModelException() {
    }

    /**
     * Creates a new instance of ModelException.
     *
     * @param  message  the detail message.
     */
    public ModelException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of ModelException.
     *
     * @param  cause  the cause.
     */
    public ModelException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new instance of ModelException.
     *
     * @param  message  the detail message.
     * @param  cause    the cause.
     */
    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }
}

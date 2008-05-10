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
 * $Id$
 */

package com.bluemarsh.graphmaker.core.model;

import java.io.IOException;

/**
 * A ModelAccess provides the means for reading a Model from persistent
 * storage, and subsequently writing a modified Model back to storage.
 *
 * @author Nathan Fiedler
 */
public interface ModelAccess {

    /**
     * Returns the model source assigned to this model access instance.
     *
     * @return  model source.
     */
    ModelSource getSource();

    /**
     * Deserializes the model from the assigned model source, including
     * its vertices, edges, and properties.
     *
     * @return  the model restored from the model source.
     * @throws  IOException
     *          if there was a problem reading the data.
     */
    Model read() throws IOException;

    /**
     * Assign a model source to this access instance.
     *
     * @param  source  the model source.
     */
    void setSource(ModelSource source);

    /**
     * Writes the model elements to the storage device represented by
     * the assigned model source.
     *
     * @param  model  from which elements are gathered.
     * @throws  IOException
     *          if there was a problem writing the data.
     */
    void write(Model model) throws IOException;
}

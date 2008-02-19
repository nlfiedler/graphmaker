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
 * are Copyright (C) 2007. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: Component.java 2869 2007-03-02 08:19:33Z nfiedler $
 */

package com.bluemarsh.graphmaker.core.model;

import java.beans.PropertyChangeListener;

/**
 * A Component is any object which is stored in the graph model, including
 * edges and vertices. It has a label and supports arbitrary properties and
 * property change listeners.
 *
 * @author  Nathan Fiedler
 */
public interface Component {
    /** Property name for the 'cost' property. */
    public static final String PROP_COST = "cost";
    /** Property name for the 'label' property. */
    public static final String PROP_LABEL = "label";

    /**
     * Add a PropertyChangeListener to the listener list.
     *
     * @param  listener  the PropertyChangeListener to be added.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Returns the value of the property with the specified key. Only
     * properties added with <code>putClientProperty</code> will return
     * a non-null value.
     *
     * @param  key  the key being queried.
     * @return  the value of this property or <code>null</code>.
     */
    Object getClientProperty(Object key);

    /**
     * Returns the cost of the edge.
     *
     * @return distance between the two endpoints.
     */
    double getCost();

    /**
     * Returns the string label for this edge. This will never return a
     * null reference, as it will create an empty string if there is not
     * a label already set.
     *
     * @return  label for this edge.
     */
    String getLabel();

    /**
     * Returns the component model this component belongs to.
     *
     * @return  component model for this component.
     */
    Model getModel();

    /**
     * Adds an arbitrary key/value "client property" to this component.
     *
     * <p>The <code>get/putClientProperty</code> methods provide access
     * to a small per-instance hashtable. Callers can use
     * get/putClientProperty to annotate components that were created
     * by another module.</p>
     *
     * <p>If value is <code>null</code> this method will remove the
     * property. Changes to client properties are reported with
     * <code>PropertyChange</code> events. The name of the property
     * (for the sake of PropertyChange events) is
     * <code>key.toString()</code>.</p>
     *
     * @param  key    the new client property key.
     * @param  value  the new client property value; if
     *                <code>null</code> the property will be removed.
     */
    void putClientProperty(Object key, Object value);

    /**
     * Remove a PropertyChangeListener from the listener list.
     *
     * @param  listener  the PropertyChangeListener to be removed.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Sets the cost of the edge. Once the cost is set it will not be
     * modified until subsequent calls to this method.
     *
     * @param  cost  new cost for this edge.
     */
    void setCost(double cost);

    /**
     * Sets the string label of this edge.
     *
     * @param  label  new label for this edge.
     */
    void setLabel(String label);
}

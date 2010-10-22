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
 * are Copyright (C) 2007-2010. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */
package com.bluemarsh.graphmaker.core.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * Class AbstractComponent is an abstract implementation of the Component
 * interface. It provides some of the basic behavior of components.
 *
 * @author Nathan Fiedler
 */
public abstract class AbstractComponent implements Component {

    /** Handles property change listeners and sending events. */
    protected final PropertyChangeSupport propSupport;
    /** Map of the client properties set in this instance. */
    private final Map<Object, Object> propertiesMap;
    /** User-defined cost of this component. */
    private double cost;
    /** User-defined label for this component. This can be shown in the
     * editor to help identify this component. */
    private String label;
    /** The owning model for this component. */
    private Model model;

    /**
     * Creates a new instance of AbstractComponent.
     */
    public AbstractComponent() {
        propSupport = new PropertyChangeSupport(this);
        propertiesMap = new HashMap<Object, Object>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propSupport.addPropertyChangeListener(listener);
    }

    @Override
    public Object getClientProperty(Object key) {
        synchronized (propertiesMap) {
            return propertiesMap.get(key);
        }
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public String getLabel() {
        if (label == null) {
            label = "";
        }
        return label;
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void putClientProperty(Object key, Object value) {
        Object oldValue;
        synchronized (propertiesMap) {
            oldValue = propertiesMap.get(key);
            if (value != null) {
                propertiesMap.put(key, value);
            } else if (oldValue != null) {
                propertiesMap.remove(key);
            } else {
                // Nothing changed, do not fire events.
                return;
            }
        }
        propSupport.firePropertyChange(key.toString(), oldValue, value);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void setCost(double cost) {
        double old = this.cost;
        this.cost = cost;
        propSupport.firePropertyChange(PROP_COST, old, cost);
    }

    @Override
    public void setLabel(String label) {
        String old = this.label;
        this.label = label;
        propSupport.firePropertyChange(PROP_LABEL, old, label);
    }

    /**
     * Sets the model for this component.
     *
     * @param  model  the component model.
     */
    protected void setModel(Model model) {
        this.model = model;
    }
}

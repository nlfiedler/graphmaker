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
 * $Id$
 */

package com.bluemarsh.graphmaker.ui.nodes;

import com.bluemarsh.graphmaker.core.model.Component;
import com.bluemarsh.graphmaker.core.util.Arrays;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport.Reflection;
import org.openide.nodes.Sheet;
import org.openide.nodes.Sheet.Set;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;

/**
 * Class ComponentNode is the base class for all Nodes that represent a
 * Component in the graph model, such as edges and vertices.
 *
 * @param  T  an extension of Component interface.
 * @author  Nathan Fiedler
 */
public abstract class ComponentNode<T extends Component> extends AbstractNode
        implements PropertyChangeListener {
    /** The model component this node represents. */
    private T component;
    /** Where objects can be registered for lookup. */
    private InstanceContent lookupContent;

    /**
     * Creates a new instance of ComponentNode.
     *
     * @param  component  the model component to represent.
     */
    public ComponentNode(T component) {
        this(Children.LEAF, component);
    }

    /**
     * Creates a new instance of ComponentNode.
     *
     * @param  children   children heirarchy.
     * @param  component  the model component to represent.
     */
    public ComponentNode(Children children, T component) {
        this(children, Lookup.EMPTY, component);
    }

    /**
     * Creates a new instance of ComponentNode.
     *
     * @param  lookup     Lookup to incorporate into Node's Lookup.
     * @param  component  the model component to represent.
     */
    public ComponentNode(Lookup lookup, T component) {
        this(Children.LEAF, lookup, component);
    }

    /**
     * Creates a new instance of ComponentNode.
     *
     * @param  children   children heirarchy.
     * @param  lookup     Lookup to incorporate into Node's Lookup.
     * @param  component  the model component to represent.
     */
    public ComponentNode(Children children, Lookup lookup, T component) {
        this(children, lookup, new InstanceContent(), component);
    }

    /**
     * Constructor hack to allow us to create our own lookup.
     *
     * @param  children  children heirarchy.
     * @param  lookup    lookup to incorporate into Node's Lookup.
     * @param  content   initial lookup content.
     */
    private ComponentNode(Children children, Lookup lookup,
            InstanceContent content, T component) {
        super(children, createLookup(lookup, content));
        lookupContent = content;
        this.component = component;
        component.addPropertyChangeListener(this);
    }

    /**
     * Creates the Lookup for this node.
     *
     * @param  lookup   the Lookup provided by the client.
     * @param  content  to which this node adds objects.
     * @return  the Lookup for this node.
     */
    private static Lookup createLookup(Lookup lookup, InstanceContent content) {
        return new ProxyLookup(new Lookup[] {
            lookup,
            new AbstractLookup(content),
        });
    }

    /**
     * Creates a node property of the given key (same as the column keys)
     * and a specific getter method on the given object.
     *
     * @param  key     property name (same as matching column).
     * @param  inst    object on which to reflect.
     * @param  getter  name of getter method for property value.
     * @param  setter  name of setter method for property value (may be null).
     * @return  new property.
     */
    @SuppressWarnings("unchecked")
    protected static Node.Property createProperty(String key, Object inst,
            String getter, String setter) {
        Property prop = null;
        try {
            prop = new Reflection(inst, String.class, getter, setter);
            prop.setName(key);
            prop.setDisplayName(NbBundle.getMessage(DefaultVertexNode.class,
                    "CTL_ComponentNode_Property_Name_" + key));
            prop.setShortDescription(NbBundle.getMessage(DefaultVertexNode.class,
                    "CTL_ComponentNode_Property_Desc_" + key));
        }  catch (NoSuchMethodException nsme) {
            ErrorManager.getDefault().notify(nsme);
            prop = new ExceptionProperty(nsme);
        }
        return prop;
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        Set set = sheet.get(Sheet.PROPERTIES);
        set.put(createProperty(Component.PROP_COST, component,
                "getCost", "setCost"));
        set.put(createProperty(Component.PROP_LABEL, component,
                "getLabel", "setLabel"));
        return sheet;
    }

    @Override
    public Action[] getActions(boolean context) {
        Action[] superA = super.getActions(context);
        Action[] subA = getNodeActions();
        return (Action[]) Arrays.join(superA, subA);
    }

    /**
     * Returns the model component this node represents.
     *
     * @return  model component.
     */
    public T getComponent() {
        return component;
    }

    @Override
    public String getDisplayName() {
        return component.getLabel();
    }

    /**
     * Returns the lookup content to which objects can be added.
     *
     * @return  lookup content.
     */
    protected InstanceContent getLookupContent() {
        return lookupContent;
    }

    /**
     * Returns the actions for this Node.
     *
     * @return  node actions, may be null.
     */
    protected abstract Action[] getNodeActions();

    public void propertyChange(PropertyChangeEvent event) {
        String name = event.getPropertyName();
        if (name.equals(Component.PROP_LABEL)) {
            fireDisplayNameChange(null, null);
        }
        // Need to ignore the other random properties that are not in our
        // "defined property set", otherwise exceptions occur.
    }
}

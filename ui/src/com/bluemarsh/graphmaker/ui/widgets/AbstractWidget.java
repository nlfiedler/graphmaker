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

package com.bluemarsh.graphmaker.ui.widgets;

import com.bluemarsh.graphmaker.core.model.Component;
import com.bluemarsh.graphmaker.core.model.Model;
import com.bluemarsh.graphmaker.ui.nodes.NodeFactory;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;

/**
 * Class AbstractWidget is the base class for all graph widgets.
 *
 * @param  T  an extension of Component.
 * @author Nathan Fiedler
 */
public abstract class AbstractWidget<T extends Component> extends Widget
        implements /*PopupMenuProvider,*/ PropertyChangeListener {
    /** The component this widget represents; may be null. */
    private T component;
    /** The Lookup for this widget. */
    private Lookup widgetLookup;
    /** The content of our customized Lookup. */
    private InstanceContent lookupContent;
    /** The Node for the component, if it has been created. */
    private Node componentNode;
    /** Used to weakly listen to the component. */
    private PropertyChangeListener weakPropertyListener;

    /**
     * Creates a new instance of AbstractWidget.
     *
     * @param  scene      the widget Scene.
     * @param  component  the component to represent.
     * @param  lookup     the Lookup for this widget.
     */
    public AbstractWidget(Scene scene, T component, Lookup lookup) {
        super(scene);
        this.component = component;
        lookupContent = new InstanceContent();
        widgetLookup = new ProxyLookup(new Lookup[] {
            new AbstractLookup(lookupContent),
            lookup
        });
        if (component != null) {
            lookupContent.add(component);
        }
        if (component != null) {
            weakPropertyListener = WeakListeners.propertyChange(this, component);
            component.addPropertyChangeListener(weakPropertyListener);
        }
//        getActions().addAction(ActionFactory.createPopupMenuAction(this));
//        getActions().addAction(new WidgetAction.Adapter() {
//            @Override
//            public WidgetAction.State keyReleased(Widget widget,
//                    WidgetAction.WidgetKeyEvent event) {
//                // Check if we are selected, otherwise ignore the event.
//                if (event.getKeyCode() == KeyEvent.VK_DELETE &&
//                        getState().isSelected()) {
//                    deleteComponent();
//                    return WidgetAction.State.CONSUMED;
//                }
//                return super.keyTyped(widget, event);
//            }
//        });
    }

    /**
     * Deletes the model component. Subclasses should in general avoid
     * overriding this method and creating another transaction, as that
     * will create another undoable edit on the undo/redo queue. Instead,
     * override the postDeleteComponent(Model) method, which is invoked
     * during the transaction, so any changes to the model will be
     * captured in a single undoable edit.
     */
    protected void deleteComponent() {
        if (component == null) {
            return;
        }
        component.removePropertyChangeListener(weakPropertyListener);
        Model model = component.getModel();
        try {
            model.startTransaction();
            model.remove(component);
            postDeleteComponent(model);
        } finally {
            try {
                model.endTransaction();
            } catch (IOException ioe) {
                ErrorManager.getDefault().notify(ioe);
            }
        }
    }

    /**
     * Subclasses may override this method to make additional changes to
     * the model within the same transaction as the one used to delete
     * the model component.
     *
     * @param  model  the model that is in transaction.
     */
    protected void postDeleteComponent(Model model) {
        // Do nothing here, as this is exclusively for subclasses to override.
    }

    /**
     * Locates the TopComponent parent of the view containing the Scene
     * that owns this widget, if possible.
     *
     * @return  the parent TopComponent, or null if not found.
     */
    protected TopComponent findTopComponent() {
        return (TopComponent) SwingUtilities.getAncestorOfClass(
                TopComponent.class, getScene().getView());
    }

    public Lookup getLookup() {
        return widgetLookup;
    }

    /**
     * Return the content of this widget's custom Lookup. Subclasses may
     * add objects to this content, thereby altering the contents of the
     * Lookup associated with this widget.
     *
     * @return  Lookup content.
     */
    protected InstanceContent getLookupContent() {
        return lookupContent;
    }

    /**
     * Returns a Node for the component that this widget represents.
     * If this widget does not have an assigned component, then this
     * returns an AbstractNode with no interesting properties.
     *
     * @return  the Node for this widget.
     */
    public synchronized Node getNode() {
        if (componentNode == null) {
            if (component == null) {
                // No component? Then supply a dummy node.
                componentNode = new AbstractNode(Children.LEAF);
            } else {
                // Use the factory to construct the Node.
                NodeFactory factory = NodeFactory.getDefault();
                componentNode = factory.create(component);
            }
        }
        return componentNode;
    }

//    public JPopupMenu getPopupMenu(Widget widget, Point point) {
//        Node node = getNode();
//        if (node != null) {
//            TopComponent tc = findTopComponent();
//            Lookup lookup;
//            if (tc != null) {
//                // Activate the node just as any explorer view would do.
//                tc.setActivatedNodes(new Node[] { node });
//                // To get the explorer actions enabled, must have the
//                // lookup from the parent TopComponent.
//                lookup = tc.getLookup();
//            } else {
//                lookup = Lookup.EMPTY;
//            }
//            Action[] actions = node.getActions(true);
//            return Utilities.actionsToPopup(actions, lookup);
//        }
//        return null;
//    }

    /**
     * Return the component this widget represents.
     *
     * @return  component for this widget; may be null.
     */
    public T getComponent() {
        return component;
    }

//    /**
//     * Indicates if the given model component is the one this widget
//     * represents. Useful for listeners that want to check the source
//     * of the event before responding to it.
//     *
//     * @param  node  the model component.
//     * @return  true if same, false otherwise.
//     */
//    protected boolean isSameComponent(Component node) {
//        return node != null && node.equals(getComponent());
//    }

    protected void notifyStateChanged(ObjectState oldState, ObjectState state) {
        super.notifyStateChanged(oldState, state);
        if (state.isSelected()) {
            TopComponent tc = findTopComponent();
            if (tc != null) {
                Node node = getNode();
                tc.setActivatedNodes(new Node[] { node });
            }
        }
        repaint();
    }

    public void propertyChange(PropertyChangeEvent event) {
        // TODO: update the widget in an appropriate manner
    }

//    protected void paintChildren() {
//        super.paintChildren();
//
//        if (getState().isSelected()) {
//            Graphics2D g2 = getGraphics();
//
//            Object oldStrokeControl = g2.getRenderingHint(RenderingHints
//                    .KEY_STROKE_CONTROL);
//            Stroke oldStroke = g2.getStroke();
//            Paint oldPaint = g2.getPaint();
//
//            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
//                    RenderingHints.VALUE_STROKE_PURE);
//
//            g2.setStroke(SELECTION_STROKE);
//            g2.setPaint(SELECTION_PAINT);
//
//            g2.draw(createSelectionShape());
//
//            g2.setStroke(oldStroke);
//            g2.setPaint(oldPaint);
//
//            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
//                    oldStrokeControl);
//        }
//    }
//
//    /**
//     * Return the shape to be used to show the selection of this widget.
//     *
//     * @return  selection shape.
//     */
//    protected Shape createSelectionShape() {
//        Rectangle rect = getBounds();
//        return new Rectangle2D.Double(rect.x + 1, rect.y + 1, rect.width - 2,
//                rect.height - 2);
//    }

//    /**
//     * Invoked when the model component has changed in some way.
//     * Subclasses should override this method to update their content.
//     */
//    protected void updateContent() {
//    }

//    /**
//     * Check if this widget should update its content based on the given
//     * component event. If so, the contents and scene validation will be
//     * performed on the event dispatching thread.
//     *
//     * @param  event  component event.
//     */
//    private void checkUpdate(ComponentEvent event) {
//        Object src = event.getSource();
//        if (src.equals(component)) {
//            Runnable updater = new Runnable() {
//                public void run() {
//                    updateContent();
//                    getScene().validate();
//                }
//            };
//            if (EventQueue.isDispatchThread()) {
//                updater.run();
//            } else {
//                EventQueue.invokeLater(updater);
//            }
//        }
//    }
}

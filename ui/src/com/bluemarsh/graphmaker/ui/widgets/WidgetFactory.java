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
 * $Id: WidgetFactory.java 2907 2007-03-07 09:42:43Z nfiedler $
 */

package com.bluemarsh.graphmaker.ui.widgets;

import com.bluemarsh.graphmaker.core.model.Component;
import com.bluemarsh.graphmaker.core.model.Edge;
import com.bluemarsh.graphmaker.core.model.Vertex;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;

/**
 * A factory for creating widgets to represent model components, such as
 * edges and vertices.
 *
 * @author  Nathan Fiedler
 */
public abstract class WidgetFactory {
    /** The default instance of the WidgetFactory. */
    private static WidgetFactory defaultFactory;

    /**
     * Retrieves the default implementation of the WidgetFactory.
     *
     * @return  node factory implementation.
     */
    public static synchronized WidgetFactory getDefault() {
        if (defaultFactory == null) {
            defaultFactory = new DefaultWidgetFactory();
        }
        return defaultFactory;
    }

    /**
     * Creates a Widget to represent the given model component.
     *
     * @param  scene      the widgets will be created in this Scene.
     * @param  component  the component to represent.
     * @return  the new widget.
     */
    public abstract Widget createWidget(Scene scene, Component component);

    /**
     * Creates a Widget to represent the given model component.
     *
     * @param  scene      the widgets will be created in this Scene.
     * @param  component  the component to represent.
     * @param  lookup     the Lookup for the widget.
     * @return  the new widget.
     */
    public abstract Widget createWidget(Scene scene, Component component,
            Lookup lookup);

    /**
     * Default implementation of WidgetFactory.
     */
    private static class DefaultWidgetFactory extends WidgetFactory {

        @Override
        public Widget createWidget(Scene scene, Component component) {
            return createWidget(scene, component, Lookup.EMPTY);
        }

        @Override
        public Widget createWidget(Scene scene, Component component,
                Lookup lookup) {
            Widget widget = null;
            if (component instanceof Edge) {
                widget = new EdgeWidget(scene, (Edge) component, lookup);
            } else if (component instanceof Vertex) {
                widget = new VertexWidget(scene, (Vertex) component, lookup);
            }
            if (widget != null) {
                prepareWidget(scene, widget, component);
            }
            return widget;
        }

        /**
         * Perform additional preparation on the widget now that it has been
         * created, including adding actions and mapping it in the scene.
         *
         * @param  scene      the Scene for the widget.
         * @param  widget     the widget to prepare.
         * @param  component  the component for the widget; may be null.
         */
        private void prepareWidget(Scene scene, Widget widget, Component component) {
// XXX: this entire method should probably move to GraphMakerScene, and
//      subsequently, no need to pass Scene to the factory methods
//            if (scene instanceof ExScene) {
//                // Add the object selection action to the widget.
//                widget.getActions().addAction(((ExScene) scene).getSelectAction());
//            }
            if (scene instanceof ObjectScene) {
// XXX: if GraphScene, may need to do extra things
                ObjectScene os = (ObjectScene) scene;
                if (component != null) {
                    // Add the object-widget mapping in the scene.
                    List<Widget> widgets = os.findWidgets(component);
                    if (widgets == null) {
                        widgets = new ArrayList<Widget>();
                    } else {
                        // Remove the original mapping.
                        os.removeObject(component);
                        // The List that comes back is immutable...
                        widgets = new ArrayList<Widget>(widgets);
                    }
                    widgets.add(widget);
                    os.addObject(component, widgets.toArray(
                            new Widget[widgets.size()]));
                }
            }
        }
    }
}

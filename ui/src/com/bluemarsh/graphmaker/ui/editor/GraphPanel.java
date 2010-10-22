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

package com.bluemarsh.graphmaker.ui.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 * Contains the scrollable view that displays the widgets which represent
 * the edges and vertices of the graph model.
 *
 * @author Nathan Fiedler
 */
public class GraphPanel extends JPanel  {
    /** silence compiler warnings */
    private static final long serialVersionUID = 1L;
    /** Component which contains the widget scene. */
    private JComponent sceneView;
    /** That which contains everything else. */
    private Widget contentWidget;
    /** The main content layer, showing edges and vertices. */
    private Widget mainLayer;
    /** Layer for handling drag and drop operations. */
//    private DragOverSceneLayer dragLayer;
    /** Manages the zoom level. */
    private ZoomManager zoomer;

    /**
     * Creates a new instance of GraphPanel.
     */
    public GraphPanel() {
        super(new BorderLayout());
        Scene scene = new GraphMakerScene();
        scene.createView();
        scene.setBackground(Color.WHITE);
        zoomer = new ZoomManager(scene);

        contentWidget = new Widget(scene);

        mainLayer = new LayerWidget(scene);
        mainLayer.addChild(contentWidget);
        mainLayer.setPreferredLocation(new Point(0, 0));
        mainLayer.setBackground(Color.WHITE);
        scene.addChild(mainLayer);
        
//        mDragLayer = scene.getDragOverLayer();
//        scene.addChild(mDragLayer);

        sceneView = scene.getView();
        JScrollPane panel = new JScrollPane ();
        panel.setBounds(getBounds());
        panel.setViewportView(sceneView);
        add(panel, BorderLayout.CENTER);
    }

    /**
     * Adds the graph actions to the given toolbar (no separators are
     * added to either the beginning or end).
     *
     * @param  toolbar  to which the actions are added.
     */
    public void addToolbarActions(JToolBar toolbar) {
        zoomer.addToolbarActions(toolbar);
    }

    /**
     * Return the view content, suitable for printing (i.e. without a
     * scroll pane, which would result in the scroll bars being printed).
     *
     * @return  the view content, sans scroll pane.
     */
    public JComponent getContent() {
        return sceneView;
    }

    /**
     * Return the ZoomManager for this GraphView instance.
     *
     * @return  the zoom manager.
     */
    public ZoomManager getZoomManager() {
        return zoomer;
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        // Ensure the graph widgets have the focus.
        sceneView.requestFocus();
    }

    @Override
    public boolean requestFocusInWindow() {
        super.requestFocusInWindow();
        // Ensure the graph widgets have the focus.
        return sceneView.requestFocusInWindow();
    }
}

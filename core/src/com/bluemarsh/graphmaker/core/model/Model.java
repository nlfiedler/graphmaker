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
 * are Copyright (C) 1999-2007. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.graphmaker.core.model;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.swing.event.UndoableEditListener;

/**
 * A Model defines the graph data structure, representing the set of
 * vertices and edges, and their relationships to one another.
 *
 * @author  Nathan Fiedler
 */
public interface Model {

    /**
     * Add a ModelListener to the listener list.
     *
     * @param  listener  the ModelListener to be added.
     */
    void addModelListener(ModelListener listener);

    /**
     * Add a PropertyChangeListener to the listener list.
     *
     * @param  listener  the PropertyChangeListener to be added.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Add an UndoableEditListener to the listener list.
     *
     * @param  listener  the UndoableEditListener to be added.
     */
    void addUndoableEditListener(UndoableEditListener listener);

    /**
     * Adds the given edge to the model.
     *
     * @param  edge  edge to add to model.
     */
    void addEdge(Edge edge);

    /**
     * Adds the given vertex to the model.
     *
     * @param  vertex  Vertex to add to model.
     */
    void addVertex(Vertex vertex);

    /**
     * Terminate the currently open transaction, if any. It is safe to
     * call this method at any time, whether a transaction is open or not.
     * The changes made will be rolled back, and no events will be sent.
     * The model should return to the same state prior to the transaction
     * being started.
     */
    void cancelTransaction();

    /**
     * Close the currently open transaction, commiting the changes to
     * the model and dispatching change events to any listeners.
     *
     * @throws  IOException
     *          if there was a problem committing the changes.
     */
    void endTransaction() throws IOException;

    /**
     * Find the edge in the model between the two given vertices.
     * For directed edges, the ordering of the vertices is important.
     *
     * @param  source  starting vertex of the edge to find.
     * @param  target  ending vertex of the edge to find.
     * @return  edge, or null if not found.
     */
    Edge findEdge(Vertex source, Vertex target);

    /**
     * Find a vertex such that the given coordinates fall within the
     * bounds of the vertex.
     *
     * @param  x  x coordinate.
     * @param  y  y coordinate.
     * @param  z  z coordinate.
     * @return  vertex, or null if no found.
     */
    Vertex findVertex(int x, int y, int z);

    /**
     * Computes the list of edges connected to the given vertex.
     *
     * @param  vertex        Vertex for which to find edges.
     * @param  directedOnly  true to find only edges leaving vertex.
     * @return  list of edges (empty if none).
     */
    List<Edge> findAdjacentEdges(Vertex vertex, boolean directedOnly);

    /**
     * Computes the list of adjacent vertices connected to the given
     * vertex. The connection is only considered for directed edges
     * leaving the given vertex, or undirected edges.
     *
     * @param  vertex  vertex for which to find adjacent vertices.
     * @return  list of vertices (empty if none).
     */
    List<Vertex> findAdjacentVertices(Vertex vertex);

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
     * Return an unmodifiable list of all the edges in this model.
     *
     * @return  list of edges in model.
     */
    List<Edge> getEdges();

    /**
     * Return an unmodifiable list of all the vertices in this model.
     *
     * @return  list of vertices in model.
     */
    List<Vertex> getVertices();

    /**
     * Indicates if the model is currently in a transaction.
     *
     * @return  true if a transaction is open, false otherwise.
     */
    boolean isInTransaction();

    /**
     * Returns the set of keys for the current client properties.
     *
     * @return  client property key set.
     */
    Set<Object> propertyKeys();

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
     * Remove the given component from the model.
     *
     * @param  component  the component to be removed.
     */
    void remove(Component component);

    /**
     * Remove the given edge from the model.
     *
     * @param  edge  the edge to be removed.
     */
    void removeEdge(Edge edge);

    /**
     * Remove a ModelListener from the listener list.
     *
     * @param  listener  the ModelListener to be removed.
     */
    void removeModelListener(ModelListener listener);

    /**
     * Remove a PropertyChangeListener from the listener list.
     *
     * @param  listener  the PropertyChangeListener to be removed.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove an UndoableEditListener from the listener list.
     *
     * @param  listener  the UndoableEditListener to be removed.
     */
    void removeUndoableEditListener(UndoableEditListener listener);

    /**
     * Remove the given vertex from the model.
     *
     * @param  vertex  the vertex to be removed.
     */
    void removeVertex(Vertex vertex);

    /**
     * Begin a transaction for making changes to the model. Changes
     * must be made from within a transaction, and only one transaction
     * can be open at a time. If a transaction is already in progress,
     * the calling thread will be blocked until the transaction is
     * completed, or the thread is interrupted.
     */
    void startTransaction();
}

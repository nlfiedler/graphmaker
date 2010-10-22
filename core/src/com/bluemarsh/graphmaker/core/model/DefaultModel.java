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
 * are Copyright (C) 1999-2010. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */
package com.bluemarsh.graphmaker.core.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.openide.ErrorManager;

/**
 * The default implementation of the Model interface. Uses an adjacency
 * list data structure to maintain the vertex and edge relationships.
 *
 * @author  Nathan Fiedler
 */
public class DefaultModel extends AbstractModel {

    /** List of vertices in the graph. */
    private List<Vertex> vertexList;
    /** List of edges in the graph. */
    private List<Edge> edgeList;
    /** Adjacency list structure. The index into the vector is the
     * source vertex. The int[] array contains a list of vertex indices,
     * each one representing the target vertex adjacent to the source
     * vertex the int[] array is associated with. */
    private List<int[]> adjacencies;

    /**
     * Creates a new instance of DefaultModel.
     */
    public DefaultModel() {
        vertexList = new LinkedList<Vertex>();
        edgeList = new LinkedList<Edge>();
        adjacencies = new ArrayList<int[]>();
    }

    /**
     * Used to put an "edge" between to vertices, without really
     * adding an edge. This only does the connection in one direction
     * (i.e. a directed edge).
     *
     * @param  from  starting vertex
     * @param  to    ending vertex
     */
    protected void addAdjacency(Vertex from, Vertex to) {
        checkInTransaction();
        int fromIdx = vertexList.indexOf(from);
        int toIdx = vertexList.indexOf(to);
        if (fromIdx == -1 || toIdx == -1 || fromIdx >= adjacencies.size()) {
            throw new IllegalStateException("vertices not in model");
        }
        // Get the adjlist for the "from" vertex.
        // Set aside a new adjlist.
        int[] list = adjacencies.get(fromIdx);
        if (list == null) {
            list = new int[1];
        } else {
            // Otherwise we need to make a new array with one more
            // element and copy over the old array data.
            int size = list.length;
            int[] newList = new int[size + 1];
            System.arraycopy(list, 0, newList, 0, size);
            list = newList;
        }
        // Now assign last element to index of "to" vertex.
        // Set new adjacency list in adjList vector.
        list[list.length - 1] = toIdx;
        adjacencies.set(fromIdx, list);
    }

    @Override
    public void addEdge(Edge edge) {
        checkInTransaction();
        if (edge == null) {
            throw new IllegalArgumentException("edge must not be null");
        }
        Vertex source = edge.getSource();
        Vertex target = edge.getTarget();
        // Validate to ensure the model does not become corrupted.
        int si = vertexList.indexOf(source);
        int ti = vertexList.indexOf(target);
        if (si == -1 || ti == -1 || si >= adjacencies.size()
                || ti >= adjacencies.size()) {
            throw new IllegalStateException("vertices not in model");
        }
        if (source.equals(target)) {
            throw new IllegalStateException("edge endpoints are same");
        }
        if (findEdge(source, target) != null
                || (!edge.isDirected() && findEdge(target, source) != null)) {
            throw new IllegalArgumentException("edge already exists");
        }

        // Add the new edge to the list and make the source vertex
        // adjacent to the target vertex. If the edge is undirected,
        // then make target adjacent to source, as well.
        edgeList.add(edge);
        addAdjacency(source, target);
        if (!edge.isDirected()) {
            addAdjacency(target, source);
        }
        fireModelEvent(new ModelEvent(this, ModelEventType.EDGE_ADDED));
        fireUndoableEdit(new EdgeAddUndoableEdit(this, edge));
    }

    @Override
    public void addVertex(Vertex vertex) {
        checkInTransaction();
        if (vertex == null) {
            throw new IllegalArgumentException("vertex must not be null");
        }
        vertexList.add(vertex);
        // Allocate space for the new vertex, to be defined later.
        adjacencies.add(null);
        fireModelEvent(new ModelEvent(this, ModelEventType.VERTEX_ADDED));
        fireUndoableEdit(new VertexAddUndoableEdit(this, vertex));
    }

    @Override
    public Edge findEdge(Vertex source, Vertex target) {
        if (source == null || target == null || source == target) {
            return null;
        }
        int si = vertexList.indexOf(source);
        int ti = vertexList.indexOf(target);
        if (si == -1 || ti == -1) {
            return null;
        }
        // Perform a quick search of the adjlist, to see if the edge exists.
        int[] list = adjacencies.get(si);
        if (list == null) {
            list = adjacencies.get(ti);
            if (list == null) {
                return null;
            }
            ti = si;
        }
        int ii;
        for (ii = 0; ii < list.length; ii++) {
            if (list[ii] == ti) {
                break;
            }
        }
        if (ii == list.length) {
            return null;
        }

        // Look through all of the edges, looking for one whose "source"
        // and "target" match the parameters.
        for (Edge edge : edgeList) {
            if (edge.getSource().equals(source)
                    && edge.getTarget().equals(target)) {
                return edge;
            }
            // If the edge is undirected, then we can match
            // the source and target in the reverse order.
            if (!edge.isDirected() && edge.getSource().equals(target)
                    && edge.getTarget().equals(source)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public Vertex findVertex(int x, int y, int z) {
        for (Vertex vertex : vertexList) {
            if (vertex.contains(x, y, z)) {
                return vertex;
            }
        }
        return null;
    }

    @Override
    public List<Edge> findAdjacentEdges(Vertex vertex, boolean directedOnly) {
        List<Edge> result = new LinkedList<Edge>();
        if (directedOnly) {
            // Look for edges that contain a matching source vertex.
            for (Edge edge : edgeList) {
                if (edge.getSource().equals(vertex)) {
                    result.add(edge);
                }
            }
        } else {
            // Look for edges that contain any matching vertex.
            for (Edge edge : edgeList) {
                if (edge.getSource().equals(vertex)) {
                    result.add(edge);
                } else if (edge.getTarget().equals(vertex)) {
                    result.add(edge);
                }
            }
        }
        return result;
    }

    @Override
    public List<Vertex> findAdjacentVertices(Vertex vertex) {
        List<Vertex> result = new LinkedList<Vertex>();
        int index = vertexList.indexOf(vertex);
        if (index >= 0) {
            int[] list = adjacencies.get(index);
            if (list != null) {
                for (int ii : list) {
                    result.add(vertexList.get(ii));
                }
            }
        }
        return result;
    }

    @Override
    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edgeList);
    }

    @Override
    public List<Vertex> getVertices() {
        return Collections.unmodifiableList(vertexList);
    }

    @Override
    public void remove(Component component) {
        if (component instanceof Edge) {
            removeEdge((Edge) component);
        } else if (component instanceof Vertex) {
            removeVertex((Vertex) component);
        }
    }

    /**
     * Used to remove the relationship between to vertices.
     *
     * @param  source  starting vertex.
     * @param  target  ending vertex.
     */
    protected void removeAdjacency(Vertex source, Vertex target) {
        checkInTransaction();
        int si = vertexList.indexOf(source);
        int ti = vertexList.indexOf(target);
        if (si == -1 || ti == -1 || si >= adjacencies.size()) {
            throw new IllegalStateException("vertices not in model");
        }
        // Get the adjlist for the source vertex.
        int[] list = adjacencies.get(si);
        if (list != null) {
            int size = list.length;
            if (size > 1) {
                // If there is a list of more than one element then
                // create a new list and copy over all the elements
                // except the one pointing to the target vertex.
                int[] newList = new int[size - 1];
                int i = 0;
                int j = 0;
                // Loop through the old list...
                while (j < size) {
                    // If we don't have the element to be removed
                    // then we must copy this one to the new array.
                    // (i.e. we skip over the one to be removed)
                    try {
                        if (list[j] != ti) {
                            newList[i] = list[j];
                            i++;
                        }
                        j++;
                    } catch (ArrayIndexOutOfBoundsException aioobe) {
                        throw new IllegalStateException(
                                "source vertex not in model", aioobe);
                    }
                }
                // Save new array reference.
                adjacencies.set(si, newList);
            } else {
                // List only has one element left, remove it.
                adjacencies.set(si, null);
            }
        }
    }

    @Override
    public void removeEdge(Edge edge) {
        checkInTransaction();
        if (edge == null) {
            throw new IllegalArgumentException("edge must not be null");
        }
        if (!edgeList.remove(edge)) {
            throw new IllegalArgumentException("edge not in model");
        }
        // Remove the connections from the adjancency list.
        Vertex source = edge.getSource();
        Vertex target = edge.getTarget();
        removeAdjacency(source, target);
        if (!edge.isDirected()) {
            removeAdjacency(target, source);
        }
        fireModelEvent(new ModelEvent(this, ModelEventType.EDGE_REMOVED));
        fireUndoableEdit(new EdgeRemoveUndoableEdit(this, edge));
    }

    @Override
    public void removeVertex(Vertex vertex) {
        checkInTransaction();
        if (vertex == null) {
            throw new IllegalArgumentException("vertex must not be null");
        }
        int idx = vertexList.indexOf(vertex);
        if (idx == -1) {
            throw new IllegalArgumentException("vertex not in model");
        }
        // First remove edges connected to this vertex.
        List<Edge> el = findAdjacentEdges(vertex, false);
        if (!el.isEmpty()) {
            throw new IllegalArgumentException("vertex must not have edges");
        }
        // Next remove the vertex itself.
        vertexList.remove(idx);
        adjacencies.remove(idx);

        // Need to adjust the table, now that a row is missing.
        for (int[] list : adjacencies) {
            // For each list of target vertices, look for any
            // values greater than idx and decrement them.
            if (list != null) {
                int listSize = list.length;
                for (int j = 0; j < listSize; j++) {
                    if (list[j] > idx) {
                        list[j]--;
                    }
                }
            }
        }
        fireModelEvent(new ModelEvent(this, ModelEventType.VERTEX_REMOVED));
        fireUndoableEdit(new VertexRemoveUndoableEdit(this, vertex));
    }

    /**
     * Invokes endTransaction() without throwing exceptions (they are sent
     * to ErrorManager). This is intended only for the deserialization
     * process, and should not be used outside of the model package.
     */
    public void safeEndTransaction() {
        try {
            endTransaction();
        } catch (IOException ioe) {
            ErrorManager.getDefault().notify(ioe);
        }
    }
}

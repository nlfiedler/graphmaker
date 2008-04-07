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
 * are Copyright (C) 1999-2008. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: FibonacciHeap.java 2915 2007-03-08 08:40:49Z nfiedler $
 */

package com.bluemarsh.graphmaker.core.util;

import java.util.Stack;

/**
 * This class implements a Fibonacci heap data structure.
 * Much of the code in this class is based on the algorithms in the
 * "Introduction to Algorithms" by Cormen, Leiserson, and Rivest in
 * Chapter 21. The amortized running time of most of these methods is
 * O(1), making it a very fast data structure. Several have an actual
 * running time of O(1). removeMin() and delete() have O(log n) amortized
 * running times because they do the heap consolidation.
 * If you attempt to store nodes in this heap with key values of
 * -Infinity (Double.NEGATIVE_INFINITY) the <code>delete()</code>
 * operation may fail to remove the correct element.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a set concurrently, and at least one of the
 * threads modifies the set, it <em>must</em> be synchronized externally.
 * This is typically accomplished by synchronizing on some object that
 * naturally encapsulates the set.</p>
 *
 * @author  Nathan Fiedler
 */
public class FibonacciHeap {
    /** Points to the minimum node in the heap. */
    private Entry min;
    /** Number of nodes in the heap. */
    private int n;

    /**
     * Constructs a FibonacciHeap object that contains no elements.
     */
    public FibonacciHeap() {
    }

    /**
     * Performs a cascading cut operation. This cuts y from its parent
     * and then does the same for its parent, and so on up the tree.
     *
     * <p>Running time: O(log n); O(1) excluding the recursion.</p>
     *
     * @param  y  node to perform cascading cut on
     */
    private void cascadingCut(Entry y) {
        Entry z = y.parent;
        // if there's a parent...
        if (z != null) {
            if (y.mark) {
                // it's marked, cut it from parent
                cut(y, z);
                // cut its parent as well
                cascadingCut(z);
            } else {
                // if y is unmarked, set it marked
                y.mark = true;
            }
        }
    }

    /**
     * Removes all elements from this heap.
     */
    public void clear() {
        min = null;
        n = 0;
    }

    /**
     * Consolidates the trees in the heap by joining trees of equal
     * degree until there are no more trees of equal degree in the
     * root list.
     *
     * <p>Running time: O(log n) amortized.</p>
     */
    private void consolidate() {
        // Asize equals ceil(log2(n))
        //int Asize = (int)Math.ceil(Math.log(n) / Math.log(2));
        int Asize = n + 1;
        Entry A[] = new Entry[Asize];
        // Initialize degree array
        for (int i = 0; i < Asize; i++) {
            A[i] = null;
        }
        // Find the number of root nodes.
        int numRoots = 0;
        Entry x = min;
        if (x != null) {
            numRoots++;
            x = x.right;
            while (x != min) {
                numRoots++;
                x = x.right;
            }
        }
        // For each node in root list do...
        while (numRoots > 0) {
            // Access this node's degree..
            int d = x.degree;
            Entry next = x.right;
            // ..and see if there's another of the same degree.
            while (A[d] != null) {
                // There is, make one of the nodes a child of the other.
                Entry y = A[d];
                // Do this based on the key value.
                if (x.key > y.key) {
                    Entry temp = y;
                    y = x;
                    x = temp;
                }
                // Node y disappears from root list.
                link(y, x);
                // We've handled this degree, go to next one.
                A[d] = null;
                d++;
            }
            // Save this node for later when we might encounter another
            // of the same degree.
            A[d] = x;
            // Move forward through list.
            x = next;
            numRoots--;
        }
        // Set min to null (effectively losing the root list) and
        // reconstruct the root list from the array entries in A[].
        min = null;
        for (int i = 0; i < Asize; i++) {
            if (A[i] != null) {
                // We've got a live one, add it to root list.
                if (min != null) {
                    // First remove node from root list.
                    A[i].left.right = A[i].right;
                    A[i].right.left = A[i].left;
                    // Now add to root list, again.
                    A[i].left = min;
                    A[i].right = min.right;
                    min.right = A[i];
                    A[i].right.left = A[i];
                    // Check if this is a new min.
                    if (A[i].key < min.key) {
                        min = A[i];
                    }
                } else {
                    min = A[i];
                }
            }
        }
    }

    /**
     * The reverse of the link operation: removes x from the child
     * list of y. This method assumes that min is non-null.
     *
     * <p>Running time: O(1)</p>
     *
     * @param  x  child of y to be removed from y's child list
     * @param  y  parent of x about to lose a child
     */
    private void cut(Entry x, Entry y) {
        // remove x from childlist of y and decrement degree[y]
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;
        // reset y.child if necessary
        if (y.child == x) {
            y.child = x.right;
        }
        if (y.degree == 0) {
            y.child = null;
        }
        // add x to root list of heap
        x.left = min;
        x.right = min.right;
        min.right = x;
        x.right.left = x;
        // set parent[x] to nil
        x.parent = null;
        // set mark[x] to false
        x.mark = false;
    }

    /**
     * Decreases the key value for a heap node, given the new value
     * to take on. The structure of the heap may be changed, but will
     * not be consolidated.
     *
     * <p>Running time: O(1) amortized</p>
     *
     * @param  x  node to decrease the key of
     * @param  k  new key value for node x
     * @exception  IllegalArgumentException
     *             if k is larger than x.key value.
     */
    public void decreaseKey(Entry x, double k) {
        if (k > x.key) {
            throw new IllegalArgumentException("cannot increase key value");
        }
        x.key = k;
        Entry y = x.parent;
        if (y != null && x.key < y.key) {
            cut(x, y);
            cascadingCut(y);
        }
        if (x.key < min.key) {
            min = x;
        }
    }

    /**
     * Deletes a node from the heap given the reference to the node.
     * The trees in the heap will be consolidated, if necessary. This
     * operation may fail to remove the correct element if there are
     * nodes with key value -Infinity.
     *
     * <p>Running time: O(log n) amortized.</p>
     *
     * @param  x  node to remove from heap.
     */
    public void delete(Entry x) {
        // make x as small as possible
        decreaseKey(x, Double.NEGATIVE_INFINITY);
        // remove the smallest, which decreases n also
        removeMin();
    }

    /**
     * Tests if the Fibonacci heap is empty or not. Returns true if
     * the heap is empty, false otherwise.
     *
     * <p>Running time: O(1) actual.</p>
     *
     * @return  true if the heap is empty, false otherwise.
     */
    public boolean isEmpty() {
        return min == null;
    }

    /**
     * Inserts a new data element into the heap. No heap consolidation
     * is performed at this time, the new node is simply inserted into
     * the root list of this heap.
     *
     * <p>Running time: O(1) actual.</p>
     *
     * @param  x    data object to insert into heap.
     * @param  key  key value associated with data object.
     * @return newly created heap node.
     */
    public Entry insert(Object x, double key) {
        Entry node = new Entry(x, key);
        // concatenate node into min list
        if (min != null) {
            node.left = min;
            node.right = min.right;
            min.right = node;
            node.right.left = node;
            if (key < min.key) {
                min = node;
            }
        } else {
            min = node;
        }
        n++;
        return node;
    }

    /**
     * Make node y a child of node x.
     *
     * <p>Running time: O(1) actual.</p>
     *
     * @param  y  node to become child
     * @param  x  node to become parent
     */
    private void link(Entry y, Entry x) {
        // remove y from root list of heap
        y.left.right = y.right;
        y.right.left = y.left;
        // make y a child of x
        y.parent = x;
        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }
        // increase degree[x]
        x.degree++;
        // set mark[y] false
        y.mark = false;
    }

    /**
     * Returns the smallest element in the heap. This smallest element
     * is the one with the minimum key value.
     *
     * <p>Running time: O(1) actual.</p>
     *
     * @return  heap node with the smallest key, or null if empty.
     */
    public Entry min() {
        return min;
    }

    /**
     * Removes the smallest element from the heap. This will cause
     * the trees in the heap to be consolidated, if necessary.
     *
     * <p>Running time: O(log n) amortized.</p>
     *
     * @return  data object with the smallest key, or null if empty.
     */
    public Object removeMin() {
        Entry z = min;
        if (z != null) {
            int numKids = z.degree;
            Entry x = z.child;
            Entry tempRight;
            // for each child of z do...
            while (numKids > 0) {
                tempRight = x.right;
                // remove x from child list
                x.left.right = x.right;
                x.right.left = x.left;
                // add x to root list of heap
                x.left = min;
                x.right = min.right;
                min.right = x;
                x.right.left = x;
                // set parent[x] to null
                x.parent = null;
                x = tempRight;
                numKids--;
            }
            // remove z from root list of heap
            z.left.right = z.right;
            z.right.left = z.left;
            if (z == z.right) {
                min = null;
            } else {
                min = z.right;
                consolidate();
            }
            // decrement size of heap
            n--;
            return z.data;
        }
        return null;
    }

    /**
     * Returns the size of the heap which is measured in the
     * number of elements contained in the heap.
     *
     * <p>Running time: O(1) actual.</p>
     *
     * @return  number of elements in the heap.
     */
    public int size() {
        return n;
    }

    /**
     * Joins two Fibonacci heaps into a new one. No heap consolidation is
     * performed at this time. The two root lists are simply joined together.
     *
     * <p>Running time: O(1) actual.</p>
     *
     * @param  H1  first heap
     * @param  H2  second heap
     * @return  new heap containing H1 and H2
     */
    public static FibonacciHeap union(FibonacciHeap H1, FibonacciHeap H2) {
        FibonacciHeap H = new FibonacciHeap();
        if ((H1 != null) && (H2 != null)) {
            H.min = H1.min;
            if (H.min != null) {
                if (H2.min != null) {
                    H.min.right.left = H2.min.left;
                    H2.min.left.right = H.min.right;
                    H.min.right = H2.min;
                    H2.min.left = H.min;
                    if (H2.min.key < H1.min.key) {
                        H.min = H2.min;
                    }                   
                }
            } else {
                H.min = H2.min;
            }
            H.n = H1.n + H2.n;
        }
        return H;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        if (min == null) {
            return "FibonacciHeap=[]";
        }
        // create a new stack and put root on it
        Stack stack = new Stack();
        stack.push(min);

        StringBuffer buf = new StringBuffer(512);
        buf.append("FibonacciHeap=[");
        // do a simple breadth-first traversal on the tree
        while (!stack.empty()) {
            Entry curr = (Entry) stack.pop();
            buf.append(curr);
            buf.append(", ");
            if (curr.child != null) {
                stack.push(curr.child);
            }
            Entry start = curr;
            curr = curr.right;
            while (curr != start) {
                buf.append(curr);
                buf.append(", ");
                if (curr.child != null) {
                    stack.push(curr.child);
                }
                curr = curr.right;
            }
        }
        buf.append(']');
        return buf.toString();
    }

    /**
     * Implements a node of the Fibonacci heap. It holds the information
     * necessary for maintaining the structure of the heap. It acts as
     * an opaque token for the data element, and serves as the key to
     * retrieving the data from the heap.
     *
     * @author  Nathan Fiedler
     */
    public static class Entry {
        /** Data object for this node, holds the key value. */
        private Object data;
        /** Key value for this node. */
        private double key;
        /** Parent node. */
        private Entry parent;
        /** First child node. */
        private Entry child;
        /** Right sibling node. */
        private Entry right;
        /** Left sibling node. */
        private Entry left;
        /** Number of children of this node. */
        private int degree;
        /** True if this node has had a child removed since this node was
         * added to its parent. */
        private boolean mark;

        /**
         * Two-arg constructor which sets the data and key fields to the
         * passed arguments. It also initializes the right and left pointers,
         * making this a circular doubly-linked list.
         *
         * @param  data  data object to associate with this node
         * @param  key   key value for this data object
         */
        public Entry(Object data, double key) {
            this.data = data;
            this.key = key;
            right = this;
            left = this;
        }

        @Override
        public String toString() {
            if (true) {
                return Double.toString(key);
            } else {
                StringBuffer buf = new StringBuffer();
                buf.append("Node=[parent = ");
                if (parent != null) {
                    buf.append(Double.toString(parent.key));
                } else {
                    buf.append("---");
                }
                buf.append(", key = ");
                buf.append(Double.toString(key));
                buf.append(", degree = ");
                buf.append(Integer.toString(degree));
                buf.append(", right = ");
                if (right != null) {
                    buf.append(Double.toString(right.key));
                } else {
                    buf.append("---");
                }
                buf.append(", left = ");
                if (left != null) {
                    buf.append(Double.toString(left.key));
                } else {
                    buf.append("---");
                }
                buf.append(", child = ");
                if (child != null) {
                    buf.append(Double.toString(child.key));
                } else {
                    buf.append("---");
                }
                buf.append(']');
                return buf.toString();
            }
        }
    }
}

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
 * are Copyright (C) 2007-2008. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.graphmaker.core.util;

import java.util.Hashtable;
import java.util.Random;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the FibonacciHeap class.
 *
 * @author  Nathan Fiedler
 */
public class FibonacciHeapTest extends TestCase {

    public FibonacciHeapTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(FibonacciHeapTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Inserts a set of elements, decreases some of their keys, then
     * extracts them by key order and ensures everything comes out
     * in the order expected.
     */
    public void test_Correctness() {
        FibonacciHeap heap = new FibonacciHeap();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
        Hashtable<Integer, FibonacciHeap.Node> entries =
                new Hashtable<Integer, FibonacciHeap.Node>();
        for (int ii = 100; ii < 200; ii++) {
            Integer it = new Integer(ii);
            entries.put(it, heap.insert(it, ii));
        }
        assertFalse(heap.isEmpty());
        assertEquals(100, heap.size());
        FibonacciHeap.Node entry = entries.get(new Integer(110));
        heap.decreaseKey(entry, 50);
        entry = entries.get(new Integer(140));
        heap.decreaseKey(entry, 25);
        entry = entries.get(new Integer(160));
        heap.decreaseKey(entry, 15);
        // Last one should be the min value.
        assertEquals(entry, heap.min());
        Object o = heap.removeMin();
        assertEquals(new Integer(160), o);
        // Second last should now be the min value.
        entry = entries.get(new Integer(140));
        assertEquals(entry, heap.min());
        heap.delete(entry);
        // Remove the third smallest entry.
        entry = entries.get(new Integer(110));
        heap.delete(entry);
        // Original min value should now be the min.
        entry = entries.get(new Integer(100));
        assertEquals(entry, heap.min());
        heap.clear();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    /**
     * This is a stress test that inserts numerous random elements and
     * ensures that they come out in increasing order by value. This
     * extreme case uncovered multiple bugs in nearly every public
     * implementation of fibonacci heap.
     */
    public void test_InsertRemoveMin() {
        FibonacciHeap heap = new FibonacciHeap();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
        // Insert a known minimum value.
        heap.insert(new Integer(1), 1);
        // Insert a lot of random numbers.
        Random random = new Random();
        for (int ii = 0; ii <= 50000; ii++) {
            int r = random.nextInt();
            if (r < 0) {
                // Insure only positive values are stored.
                r = r + Integer.MAX_VALUE;
            }
            heap.insert(new Integer(r), r);
        }
        // Ensure the numbers come out in increasing order.
        int ii = 1;
        while (!heap.isEmpty()) {
            Integer v = (Integer) heap.removeMin();
            int vi = v.intValue();
            assertTrue(vi >= ii);
            ii = vi;
        }
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    public void test_Union() {
        FibonacciHeap heap1 = new FibonacciHeap();
        assertTrue(heap1.isEmpty());
        assertEquals(0, heap1.size());
        heap1.insert(new Integer(1), 1);
        heap1.insert(new Integer(2), 2);
        heap1.insert(new Integer(3), 3);
        heap1.insert(new Integer(4), 4);
        heap1.insert(new Integer(5), 5);
        assertFalse(heap1.isEmpty());
        assertEquals(5, heap1.size());
        FibonacciHeap heap2 = new FibonacciHeap();
        assertTrue(heap2.isEmpty());
        assertEquals(0, heap2.size());
        heap2.insert(new Integer(6), 6);
        heap2.insert(new Integer(7), 7);
        heap2.insert(new Integer(8), 8);
        heap2.insert(new Integer(9), 9);
        heap2.insert(new Integer(10), 10);
        assertFalse(heap2.isEmpty());
        assertEquals(5, heap2.size());
        FibonacciHeap joined = FibonacciHeap.union(heap1, heap2);
        assertFalse(joined.isEmpty());
        assertEquals(10, joined.size());
        Integer v = (Integer) joined.removeMin();
        int vi = v.intValue();
        int ii = 1;
        assertTrue(vi == ii);
        while (!joined.isEmpty()) {
            v = (Integer) joined.removeMin();
            vi = v.intValue();
            assertTrue(vi > ii);
            ii = vi;
        }
        assertTrue(joined.isEmpty());
        assertEquals(0, joined.size());
    }
}

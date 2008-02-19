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
 * $Id: FibonacciHeapTest.java 2915 2007-03-08 08:40:49Z nfiedler $
 */

package com.bluemarsh.graphmaker.core.util;

import java.util.Hashtable;
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

    public void test_FibonacciHeap() {
        FibonacciHeap heap = new FibonacciHeap();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
        Hashtable<Integer, FibonacciHeap.Entry> entries =
                new Hashtable<Integer, FibonacciHeap.Entry>();
        for (int ii = 100; ii < 200; ii++) {
            Integer it = new Integer(ii);
            entries.put(it, heap.insert(it, ii));
        }
        assertFalse(heap.isEmpty());
        assertEquals(100, heap.size());
        FibonacciHeap.Entry entry = entries.get(new Integer(110));
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
}

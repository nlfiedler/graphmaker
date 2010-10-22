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
 * are Copyright (C) 2006-2010. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */
package com.bluemarsh.graphmaker.core.util;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests the DisjointSet class.
 *
 * @author  Nathan Fiedler
 */
public class DisjointSetTest {

    @Test
    public void test_DisjointSet() {
        // TODO: write unit tests for the following public methods
        // clone
        // contains
        // containsAll
        // equals
        // get
        // hashCode
        // indexOf
        // iterator
        // lastIndexOf
        // listIterator
        // numberOfElements
        // numberOfTrees
        // size
        // subList
        // toArray
        // toString
        DisjointSet<Integer> set = new DisjointSet<Integer>(10);
        assertTrue(set.isEmpty());
        set.set(0, new Integer(1));
        set.set(1, new Integer(2));
        assertFalse(set.isEmpty());
        assertFalse(set.find(0) == set.find(1));
        set.union(0, 1);
        assertTrue(set.find(0) == set.find(1));
        set.clear();
        assertTrue(set.isEmpty());
    }
}

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
 * are Copyright (C) 2006-2007. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id: DisjointSetTest.java 2853 2007-02-25 02:09:25Z nfiedler $
 */

package com.bluemarsh.graphmaker.core.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the DisjointSet class.
 *
 * @author  Nathan Fiedler
 */
public class DisjointSetTest extends TestCase {

    public DisjointSetTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(DisjointSetTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void test_DisjointSet() {
        DisjointSet set = new DisjointSet(10);
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

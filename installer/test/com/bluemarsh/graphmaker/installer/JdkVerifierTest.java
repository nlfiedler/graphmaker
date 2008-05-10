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
 * are Copyright (C) 2005-2007. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.graphmaker.installer;

import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for the JdkVerifier class.
 *
 * @author Nathan Fiedler
 */
public class JdkVerifierTest extends TestCase {

    public JdkVerifierTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(JdkVerifierTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void test_ScanPath() {
        String home = System.getProperty("java.home");
        if (home.endsWith("jre")) {
            // Trim the "/jre" part from the path.
            home = home.substring(0, home.length() - 4);
        }
        File dir = new File(home);
        JdkVerifier jv = new JdkVerifier();
        jv.scanPath(dir);
        assertTrue("java.home has no JDK?", jv.sufficientVersion());
        home = System.getProperty("user.home");
        dir = new File(home);
        jv = new JdkVerifier();
        jv.scanPath(dir);
        assertFalse("user.home has JDK?", jv.sufficientVersion());
    }
}

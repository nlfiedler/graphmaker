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
 * $Id$
 */

package com.bluemarsh.graphmaker.ui;

import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;

/**
 * BeanInfo for the GraphDataLoader class.
 *
 * @author Nathan Fiedler
 */
public class GraphDataLoaderBeanInfo extends SimpleBeanInfo {
    private static final String ICON_DIR_BASE = "com/bluemarsh/graphmaker/ui/resources/";
    private static final int PROPERTY_extensions = 0;
    private static final int PROPERTY_displayName = 1;
    private static final int PROPERTY_representationClass = 2;

    // Bean descriptor information will be obtained from introspection.//GEN-FIRST:BeanDescriptor
    private static BeanDescriptor beanDescriptor = null;
    private static BeanDescriptor getBdescriptor(){
//GEN-HEADEREND:BeanDescriptor

        BeanDescriptor beanDescriptor = new BeanDescriptor(GraphDataLoader.class, null);
        beanDescriptor.setDisplayName(NbBundle.getMessage(
                GraphDataLoaderBeanInfo.class, "LBL_GraphDataLoader_DisplayName"));
        beanDescriptor.setShortDescription(NbBundle.getMessage(
                GraphDataLoaderBeanInfo.class, "LBL_GraphDataLoader_ShortDescription"));                              

        return beanDescriptor;     }//GEN-LAST:BeanDescriptor

    // Properties information will be obtained from introspection.//GEN-FIRST:Properties
    private static PropertyDescriptor[] properties = null;
    private static PropertyDescriptor[] getPdescriptor(){
//GEN-HEADEREND:Properties

        PropertyDescriptor[] properties = new PropertyDescriptor[3];
        try {
            properties[PROPERTY_extensions] = new PropertyDescriptor(
                    "extensions", GraphDataLoader.class,
                    "getExtensions", "setExtensions");
            properties[PROPERTY_extensions].setPreferred(true);
            properties[PROPERTY_extensions].setDisplayName(
                    NbBundle.getMessage(GraphDataLoaderBeanInfo.class,
                    "PROP_GraphDataLoader_extensions_name"));
            properties[PROPERTY_extensions].setShortDescription(
                    NbBundle.getMessage(GraphDataLoaderBeanInfo.class,
                    "PROP_GraphDataLoader_extensions_desc"));
            properties[PROPERTY_displayName] = new PropertyDescriptor(
                    "displayName", GraphDataLoader.class,
                    "getDisplayName", null);
            properties[PROPERTY_displayName].setDisplayName(
                    NbBundle.getMessage(GraphDataLoaderBeanInfo.class,
                    "PROP_GraphDataLoader_dname_name"));
            properties[PROPERTY_displayName].setShortDescription(
                    NbBundle.getMessage(GraphDataLoaderBeanInfo.class,
                    "PROP_GraphDataLoader_dname_desc"));
            properties[PROPERTY_representationClass] = new PropertyDescriptor(
                    "representationClass", GraphDataLoader.class,
                    "getRepresentationClass", null);
            properties[PROPERTY_representationClass].setExpert(true);
            properties[PROPERTY_representationClass].setDisplayName(
                    NbBundle.getMessage(GraphDataLoaderBeanInfo.class,
                    "PROP_GraphDataLoader_class_name"));
            properties[PROPERTY_representationClass].setShortDescription(
                    NbBundle.getMessage(GraphDataLoaderBeanInfo.class,
                    "PROP_GraphDataLoader_class_desc"));
        } catch (IntrospectionException ie) {
        }

        return properties;     }//GEN-LAST:Properties

    // Event set information will be obtained from introspection.//GEN-FIRST:Events
    private static EventSetDescriptor[] eventSets = null;
    private static EventSetDescriptor[] getEdescriptor(){
//GEN-HEADEREND:Events

        // Here you can add code for customizing the event sets array.

        return eventSets;     }//GEN-LAST:Events

    // Method information will be obtained from introspection.//GEN-FIRST:Methods
    private static MethodDescriptor[] methods = null;
    private static MethodDescriptor[] getMdescriptor(){
//GEN-HEADEREND:Methods

        // Here you can add code for customizing the methods array.

        return methods;     }//GEN-LAST:Methods

    private static java.awt.Image iconColor16 = null;//GEN-BEGIN:IconsDef
    private static java.awt.Image iconColor32 = null;
    private static java.awt.Image iconMono16 = null;
    private static java.awt.Image iconMono32 = null;//GEN-END:IconsDef
    private static String iconNameC16 = null;//GEN-BEGIN:Icons
    private static String iconNameC32 = null;
    private static String iconNameM16 = null;
    private static String iconNameM32 = null;//GEN-END:Icons
    private static int defaultPropertyIndex = -1;//GEN-BEGIN:Idx
    private static int defaultEventIndex = -1;//GEN-END:Idx
//GEN-FIRST:Superclass
//GEN-LAST:Superclass

    public BeanDescriptor getBeanDescriptor() {
        return getBdescriptor();
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        return getPdescriptor();
    }

    public EventSetDescriptor[] getEventSetDescriptors() {
        return getEdescriptor();
    }

    public MethodDescriptor[] getMethodDescriptors() {
        return getMdescriptor();
    }

    public int getDefaultPropertyIndex() {
        return defaultPropertyIndex;
    }

    public int getDefaultEventIndex() {
        return defaultEventIndex;
    }

    public java.awt.Image getIcon(int iconKind) {
        if (iconKind == ICON_COLOR_16x16 || iconKind == ICON_MONO_16x16) {
            return Utilities.loadImage(ICON_DIR_BASE + "graphObject.gif");
        } else {
            return Utilities.loadImage(ICON_DIR_BASE + "graphObject32.gif");
        }
    }
}

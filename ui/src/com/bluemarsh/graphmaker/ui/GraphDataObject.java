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

import java.io.IOException;
import javax.swing.Action;
import org.openide.actions.OpenAction;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.actions.SystemAction;

/**
 * Owner of the GraphMaker data files, provides cookies and editor.
 *
 * @author Nathan Fiedler
 */
public class GraphDataObject extends MultiDataObject {
    /** silence compiler warnings */
    private static final long serialVersionUID = 1L;
    /** Icon for the data node. */
    private static final String ICON_BASE_WITH_EXT =
            "com/bluemarsh/graphmaker/ui/resources/graphFile.png";
    /** The open support for this data object. */
    private transient GraphOpenSupport graphOpenSupport;
    /** Saves the document when necessary. */
    private transient GraphSaveCookie saveCookie;

    /**
     * Creates a new instance of GraphDataObject.
     */
    public GraphDataObject(FileObject fobj, UniFileLoader loader) throws
            DataObjectExistsException {
        super(fobj, loader);

        CookieSet set = getCookieSet();
        graphOpenSupport = new GraphOpenSupport(this);
        set.add(graphOpenSupport);
        saveCookie = new GraphSaveCookie(this);
    }

    @Override
    protected Node createNodeDelegate() {
        GraphDataNode node = new GraphDataNode(this);
        node.setIconBaseWithExtension(ICON_BASE_WITH_EXT);
        node.setShortDescription(NbBundle.getMessage(GraphDataObject.class,
                "LBL_GraphDataNode_desc"));
        return node;
    }

    @Override
    public void handleDelete() throws IOException {
        if (isModified()) {
            setModified(false);
        }
        getOpenSupport().getEnv().unmarkModified();
        super.handleDelete();
    }

    /**
     * Return the open support for this data object.
     *
     * @return  open support.
     */
    public GraphOpenSupport getOpenSupport() {
        return graphOpenSupport;
    }

    @Override
    public void setModified(boolean modified) {
        super.setModified(modified);
        if (modified) {
            getCookieSet().add(saveCookie);
        } else {
            getCookieSet().remove(saveCookie);
        }
    }

    /**
     * DataNode implementation for GraphMaker files.
     */
    private static class GraphDataNode extends DataNode {

        /**
         * Creates a new instance of GraphDataNode.
         *
         * @param  dobj   corresponding data object.
         */
        public GraphDataNode(GraphDataObject dobj) {
            super(dobj, Children.LEAF);
        }

        @Override
        public Action getPreferredAction() {
            return SystemAction.get(OpenAction.class);
        }
    }

    /**
     * The SaveCookie implementation for GraphMaker data files.
     */
    private static class GraphSaveCookie implements SaveCookie {
        /** The data object to be saved. */
        private GraphDataObject dataObject;

        /**
         * Creates a new instanceof GraphSaveCookie.
         *
         * @param  dobj  data object to be saved.
         */
        public GraphSaveCookie(GraphDataObject dobj) {
            dataObject = dobj;
        }

        @Override
        public boolean equals(Object other) {
            return other != null && getClass().equals(other.getClass());
        }

        @Override
        public int hashCode() {
            return getClass().hashCode();
        }

        public void save() throws IOException {
            dataObject.getOpenSupport().saveModel();
            dataObject.setModified(false);
        }
    }
}

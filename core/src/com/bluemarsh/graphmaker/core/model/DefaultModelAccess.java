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
 * are Copyright (C) 2006-2008. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 *
 * $Id$
 */

package com.bluemarsh.graphmaker.core.model;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.openide.ErrorManager;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;

/**
 * Default implementation of the ModelAccess interface, operating on
 * FileObject and using XMLEncoder to persist the model elements.
 *
 * @author Nathan Fiedler
 */
public class DefaultModelAccess implements ModelAccess {
    /** The assigned model source instance. */
    private ModelSource modelSource;
    /** If non-null, the lock used to obtain output stream. */
    private FileLock fileLock;

    /**
     * Creates a new instance of DefaultModelAccess.
     */
    public DefaultModelAccess() {
    }

    /**
     * Obtain an IntputStream from which the model can be read.
     *
     * @return  input stream from which to read.
     * @throws  IOException
     *          if error obtaining input stream.
     */
    private InputStream getInputStream() throws IOException {
        FileObject fobj = modelSource.getLookup().lookup(FileObject.class);
        if (fobj == null) {
            File file = modelSource.getLookup().lookup(File.class);
            if (file == null) {
                throw new IllegalArgumentException(
                        "ModelSource contains neither FileObject nor File");
            }
            if (!file.isFile()) {
                throw new IllegalArgumentException(
                        "ModelSource does not contain a valid File");
            }
            return new FileInputStream(file);
        }
        if (!fobj.isData()) {
            throw new IllegalArgumentException(
                    "ModelSource does not contain a valid FileObject");
        }
        return fobj.getInputStream();
    }

    /**
     * Obtain an OutputStream to which the model can be written.
     *
     * @return  output stream to which to write.
     * @throws  IOException
     *          if error obtaining output stream.
     */
    private OutputStream getOutputStream() throws IOException {
        FileObject fobj = modelSource.getLookup().lookup(FileObject.class);
        if (fobj == null) {
            File file = modelSource.getLookup().lookup(File.class);
            if (file == null) {
                throw new IllegalArgumentException(
                        "ModelSource contains neither FileObject nor File");
            }
            return new FileOutputStream(file);
        }
        fileLock = fobj.lock();
        return fobj.getOutputStream(fileLock);
    }

    public ModelSource getSource() {
        return modelSource;
    }

    public synchronized Model read() throws IOException {
        XMLDecoder decoder = null;
        Model model = null;
        try {
            InputStream is = getInputStream();
            decoder = new XMLDecoder(is);
            decoder.setExceptionListener(new ExceptionListener() {
                public void exceptionThrown(Exception e) {
                    ErrorManager.getDefault().notify(e);
                }
            });
            model = (Model) decoder.readObject();
            List<Edge> edges = model.getEdges();
            for (Edge edge : edges) {
                if (edge instanceof AbstractComponent) {
                    ((AbstractComponent) edge).setModel(model);
                }
            }
            List<Vertex> vertices = model.getVertices();
            for (Vertex vertex : vertices) {
                if (vertex instanceof AbstractComponent) {
                    ((AbstractComponent) vertex).setModel(model);
                }
            }
        } finally {
            if (decoder != null) {
                decoder.close();
            }
        }
        return model;
    }

    public void setSource(ModelSource source) {
        modelSource = source;
    }

    public synchronized void write(Model model) throws IOException {
        if (modelSource.isWritable()) {
            try {
                OutputStream os = getOutputStream();
                XMLEncoder encoder = new XMLEncoder(os);
                encoder.writeObject(model);
                encoder.close();
            } finally {
                if (fileLock != null) {
                    fileLock.releaseLock();
                    fileLock = null;
                }
            }
        } else {
            throw new IOException("model source is read-only");
        }
    }
}

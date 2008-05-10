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

package com.bluemarsh.graphmaker.core.model;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * A ModelSource encapsulates the file in which the graph model is stored.
 * It may be retrieved via the Lookup provided by this ModelSource.
 *
 * @author  Nathan Fiedler
 */
public class ModelSource implements Lookup.Provider {
    /** Cache of the ModelSource instances for each FileObject. */
    private static Map<FileObject, WeakReference<ModelSource>> foCache;
    /** Cache of the ModelSource instances for each File. */
    private static Map<File, WeakReference<ModelSource>> fileCache;
    /** The lookup from which the source comes. */
    private Lookup lookup;
    /** Indicates if this model is editable. */
    private boolean writable;

    static {
        foCache = new WeakHashMap<FileObject, WeakReference<ModelSource>>();
        fileCache = new WeakHashMap<File, WeakReference<ModelSource>>();
    }

    /**
     * Creates a new instance of ModelSource.
     *
     * @param  lookup    provides the FileObject of the model data.
     * @param  writable  true if the data file is writable.
     */
    public ModelSource(Lookup lookup, boolean writable) {
        this.writable = writable;
        this.lookup = lookup;
    }

    /**
     * Creates a ModelSource for the given File.
     *
     * @param  file      File that is the source of model data.
     * @param  writable  true if source is writable, false otherwise.
     * @return  new ModelSource instance.
     */
    public static ModelSource create(File file, boolean writable) {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }
        WeakReference<ModelSource> ref = fileCache.get(file);
        ModelSource source = ref == null ? null : ref.get();
        if (source == null) {
            source = new ModelSource(Lookups.singleton(file), writable);
            ref = new WeakReference<ModelSource>(source);
            fileCache.put(file, ref);
        }
        return source;
    }

    /**
     * Creates a ModelSource for the given FileObject.
     *
     * @param  fobj      FileObject that is the source of model data.
     * @param  writable  true if source is writable, false otherwise.
     * @return  new ModelSource instance.
     */
    public static ModelSource create(FileObject fobj, boolean writable) {
        if (fobj == null) {
            throw new IllegalArgumentException("FileObject must not be null");
        }
        WeakReference<ModelSource> ref = foCache.get(fobj);
        ModelSource source = ref == null ? null : ref.get();
        if (source == null) {
            source = new ModelSource(Lookups.singleton(fobj), writable);
            ref = new WeakReference<ModelSource>(source);
            foCache.put(fobj, ref);
        }
        return source;
    }

    /**
     * Returns the Lookup associated with this ModelSource. The Lookup
     * should contain a FileObject that represents the model data.
     *
     * @return  Lookup of this model source.
     */
    public Lookup getLookup(){
        return lookup;
    }

    /**
     * Indicates if the file containing the model data is writable.
     *
     * @return  true if the model source file is writable, false otherwise.
     */
    public boolean isWritable(){
        return writable;
    }
}

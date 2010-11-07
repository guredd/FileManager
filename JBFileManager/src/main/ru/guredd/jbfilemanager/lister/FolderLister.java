package ru.guredd.jbfilemanager.lister;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * <br>
 * JBFileManager
 * <br>
 * Folder lister.
 *
 * @author Eduard Gurskiy
 */
public class FolderLister implements ILister {

    /**
     * Lists specified folder.
     * @param type container type, should be folder
     * @param path folder path
     * @return array of items inside specified folder
     * @throws IOException in case of error
     */
    public IListedItem[] list(String type, String path) throws IOException {
        if(path == null) {
            return null;
        }
        File dir = new File(path);
        File[] filesInDir = dir.listFiles();
        if (filesInDir != null) {
            TreeSet<File> set = new TreeSet<File>(DefaultFileComparator.getInstance());
            for(int i=0;i<filesInDir.length;i++) {
                set.add(filesInDir[i]);
            }
            return buildList(set);
        } else {
            IListedItem[] result = new SimpleItem[1];
            result[0] = new SimpleItem("server has failed to list this dir",IListedItem.ERROR);
            return result;    
        }
    }

    /**
     * Sets lister detalisation mode. Only simple mode is supported.
     * @param mode mode to set
     */
    public void setMode(int mode) {
        if(mode != ILister.PREDEFINED_SIMPLE_MODE) {
            throw new UnsupportedOperationException("only PREDEFINED_SIMPLE_MODE(0) is supported");
        }
    }

    /**
     * Builds items array from sorted set of files.
     * @param set set of files
     * @return array of items
     */
    private IListedItem[] buildList(TreeSet<File> set) {
        if(set == null) {
            return null;
        }
        Object[] array = set.toArray();
        IListedItem[] result = new SimpleItem[array.length];
        String type;
        File f;
        for(int i=0;i<array.length;i++) {
            f = (File)array[i];
            if(f.isDirectory()) {
                type = IListedItem.FOLDER;
            } else {
                type = SimpleItem.getTypeByName(f.getName());
            }
            result[i] = new SimpleItem(f.getName(),type);
        }
        return result;
    }
}

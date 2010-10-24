package ru.guredd.jbfilemanager.lister;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * <br>
 * JBFileManager
 * <br>
 * ZIP archives lister.
 *
 * @author Eduard Gurskiy
 */
public class ZIPLister extends FolderLister {

    /**
     * Lists specified zip archive.
     * @param type container type
     * @param path zip archive path
     * @return array of items inside specified archive
     * @throws IOException in case of error
     */
    public IListedItem[] list(String type, String path) throws IOException {
        if(path == null) {
            return null;
        }
        int depth;
        String arr[] = path.split('.' + type + '/');
        String outpath;
        String inpath="";
        if(arr.length == 1) {
            depth = 1;
            outpath = path;
        } else {
            depth = arr[1].split("/").length + 1;
            inpath = arr[1];
            outpath = arr[0] + '.' + type;
        }

        ZipInputStream zis = null;
        TreeSet<ZipEntry> set = null;

        try {
            zis = new ZipInputStream(new FileInputStream(outpath));
            set = new TreeSet<ZipEntry>(DefaultFileComparator.getInstance());
            ZipEntry ze = null;

            while ((ze = zis.getNextEntry()) != null) {
                if(ze.getName().split("/").length == depth && ze.getName().startsWith(inpath)) {
                    zis.closeEntry();
                    set.add(ze);
                }
            }            
        } finally {
            if(zis != null) {
                zis.close();
            }
        }
        return buildList(inpath,type,set);
    }

    /**
     * Builds items array from sorted set of zip entries.
     * @param inpath path to zip container
     * @param ziptype type of zip container
     * @param set set of zip entries
     * @return array of items
     */
    private IListedItem[] buildList(String inpath, String ziptype, TreeSet<ZipEntry> set) {
        if(set == null) {
            return null;
        }
        Object[] array = set.toArray();
        IListedItem[] result = new SimpleItem[array.length];
        String name;
        String type;
        ZipEntry z;
        ZIPSimpleItem zitem;
        for(int i=0;i<array.length;i++) {
            z = (ZipEntry)array[i];
            name = z.getName();
            if(inpath != null && inpath.length() > 0) {
                name = name.replaceFirst(inpath,"");
            }
            if(name.startsWith("/")) {
                name = name.substring(1,name.length());
            }
            if(z.isDirectory()) {
                type = ziptype;
                name = name.substring(0,name.length()-1);
                zitem = new ZIPSimpleItem(name,type);
                zitem.setDirectory(true);
            } else {
                type = SimpleItem.getTypeByName(z.getName());
                zitem = new ZIPSimpleItem(name,type);
            }
            
            result[i] = zitem;
        }
        return result;
    }
}

package ru.guredd.jbfilemanager.lister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 11.10.2010
 * Time: 1:39:03
 * To change this template use File | Settings | File Templates.
 */
public class ZIPLister extends FolderLister {

    public IListedItem[] list(String path) throws IOException {
        if(path == null) {
            return null;
        }
        ZipInputStream zis = null;
        TreeSet<ZipEntry> set = null;

        try {
            zis = new ZipInputStream(new FileInputStream(path));
            set = new TreeSet<ZipEntry>(DefaultFileComparator.getInstance());
            ZipEntry ze = null;

            while ((ze = zis.getNextEntry()) != null) {
                set.add(ze);
                zis.closeEntry();
            }            
        } finally {
            if(zis != null) {
                zis.close();
            }
        }

        return buildList(set);
    }

     private IListedItem[] buildList(TreeSet<ZipEntry> set) {
        if(set == null) {
            return null;
        }
        ZipEntry[] array = (ZipEntry[])set.toArray();
        IListedItem[] result = new SimpleItem[array.length];

        for(int i=0;i<array.length;i++) {
            result[i] = new SimpleItem(array[i].getName(),SimpleItem.getTypeByName(array[i].getName()));
        }
        return result;
    }
}

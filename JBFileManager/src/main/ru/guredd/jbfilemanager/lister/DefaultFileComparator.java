package ru.guredd.jbfilemanager.lister;

import java.io.File;
import java.util.zip.ZipEntry;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * <br>
 * JBFileManager
 * <br>
 * Default comparator for files.
 *
 * @author Eduard Gurskiy
 */
public class DefaultFileComparator extends AbstractFileComparator {

    /**
     * The only singletone instance.
     */
    private static DefaultFileComparator anInstance = new DefaultFileComparator();

    /**
     * Restricted constructor.
     */
    private DefaultFileComparator() {
    }

    /**
     * @return single instance of itself
     */
    public static DefaultFileComparator getInstance() {
        return anInstance;
    }

    /**
     * Compares 2 files or zip entries.
     * Used to sort item lists returned by listers.
     * @param o1 file1
     * @param o2 file2
     * @return 1 if file1>=file2, -1 if file1<file2
     */
    public int compare(Object o1, Object o2) {

         if(o1 instanceof File && o2 instanceof File) {
            File f1 = (File) o1;
            File f2 = (File) o2;
            if(f1.isDirectory() && !f2.isDirectory()) {
                return -1;
            } else if(!f1.isDirectory() && f2.isDirectory()) {
                return 1;
            } else if(f1.isDirectory() && f2.isDirectory()) {
                return compareByName(f1.getName(),f2.getName());
            }
            if(AbstractFileComparator.SORT_NAME.equals(getMode())) {
                return compareByName(f1.getName(),f2.getName());
            } else if (AbstractFileComparator.SORT_SIZE.equals(getMode())) {
                return compareBySize(f1.length(),f2.length());
            } else if (AbstractFileComparator.SORT_MODIFIED.equals(getMode())) {
                return compareByTime(f1.lastModified(),f2.lastModified());
            } else {
                return 1;
            }
        } else if(o1 instanceof ZipEntry && o2 instanceof ZipEntry) {
            ZipEntry z1 = (ZipEntry) o1;
            ZipEntry z2 = (ZipEntry) o2;
            if(z1.isDirectory() && !z2.isDirectory()) {
                return -1;
            } else if(!z1.isDirectory() && z2.isDirectory()) {
                return 1;
            } else if(z1.isDirectory() && z2.isDirectory()) {
                return compareByName(z1.getName(),z2.getName());
            }
            if(AbstractFileComparator.SORT_NAME.equals(getMode())) {
                return compareByName(z1.getName(),z2.getName());
            } else if (AbstractFileComparator.SORT_SIZE.equals(getMode())) {
                return compareBySize(z1.getSize(),z2.getSize());
            } else if (AbstractFileComparator.SORT_MODIFIED.equals(getMode())) {
                return compareByTime(z1.getTime(),z2.getTime());
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
}

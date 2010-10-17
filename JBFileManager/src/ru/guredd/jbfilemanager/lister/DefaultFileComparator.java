package ru.guredd.jbfilemanager.lister;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.zip.ZipEntry;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 14.10.2010
 * Time: 21:44:40
 * To change this template use File | Settings | File Templates.
 */
public class DefaultFileComparator extends AbstractFileComparator {

    private static DefaultFileComparator anInstance = new DefaultFileComparator();
    
    private DefaultFileComparator() {
    }

    public static Comparator getInstance() {
        return anInstance;
    }

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
            switch(getMode()) {
                case SORT_NAME: return compareByName(f1.getName(),f2.getName());
                case SORT_SIZE: return compareBySize(f1.length(),f2.length());
                case SORT_TIME: return compareByTime(f1.lastModified(),f2.lastModified());
                default: return 0;
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
            switch(getMode()) {
                case SORT_NAME: return compareByName(z1.getName(),z2.getName());
                case SORT_SIZE: return compareBySize(z1.getSize(),z2.getSize());
                case SORT_TIME: return compareByTime(z1.getTime(),z2.getTime());
                default: return 0;
            }
        } else {
            return 0;
        }
    }
}

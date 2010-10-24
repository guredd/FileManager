package ru.guredd.jbfilemanager.lister;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * @author Eduard Gurskiy
 *
 * JBFileManager
 *
 * Abstract comparator for files.
 */
public abstract class AbstractFileComparator implements Comparator {

    /**
     * Constant for sort type "by name".
     */
    public static final String SORT_NAME = "name";
    /**
     * Constant for sort type "by size".
     */
    public static final String SORT_SIZE = "size";
    /**
     * Constant for sort type "by modification date".
     */
    public static final String SORT_MODIFIED = "modified";

    /**
     * Collator used for string comparisons.
     */
    //TODO: parameterize locale
    private Collator collator = Collator.getInstance(new Locale("ru","RU"));

    /**
     * Sort mode.
     */
    private String mode = SORT_NAME;

    /**
     * Function from Comparator interface. Compares 2 files in current case.
     * @param o1 file1
     * @param o2 file2
     * @return 1 if file1>file2, -1 if file1<file2, 0 if file1=file2
     */
    public abstract int compare(Object o1, Object o2);

    /**
     * Compare 2 names.
     * @param name1 name1
     * @param name2 name2
     * @return 1 if name1>name2, -1 if name1<name2, 0 if name1=name2
     */
    protected int compareByName(String name1, String name2) {
        return collator.compare(name1, name2);
    }

    /**
     * Compare 2 file sizes.
     * Should not return 0, because corresponding files will be considered as equal.     *
     * @param l1 size1
     * @param l2 size2
     * @return 1 if size1>=size2, -1 if size1<size2
     */
    protected int compareBySize(long l1, long l2) {
        if(l1 >= l2) {
            return 1;
        } else {
            return -1;
        } 
    }

    /**
     * Compare 2 times.
     * Should not return 0, because corresponding files will be considered as equal.     *
     * @param t1 time1
     * @param t2 time2
     * @return 1 if time1>=time2, -1 if time1<time2
     */
    protected int compareByTime(long t1, long t2) {
        if(t1 >= t2) {
            return -1;
        } else {
            return 1;
        } 
    }

    /**
     * @return sort mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode sort mode to set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }
}

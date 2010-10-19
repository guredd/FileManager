package ru.guredd.jbfilemanager.lister;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 14.10.2010
 * Time: 23:32:50
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractFileComparator implements Comparator {

    public static final String SORT_NAME = "name";
    public static final String SORT_SIZE = "size";
    public static final String SORT_MODIFIED = "modified";

    //TODO: parameterize locale
    private Collator collator = Collator.getInstance(new Locale("ru","RU"));
    private String mode = SORT_NAME;

    public abstract int compare(Object o1, Object o2);
    
    protected int compareByName(String name1, String name2) {
        return collator.compare(name1, name2);
    }

    /**
     *
     * Should not return 0, because corresponding files will be considered as equal.
     *
     * @param l1
     * @param l2
     * @return
     */
    protected int compareBySize(long l1, long l2) {
        if(l1 >= l2) {
            return 1;
        } else {
            return -1;
        } 
    }

    /**
     *
     * Should not return 0, because corresponding files will be considered as equal.
     *
     * @param t1
     * @param t2
     * @return
     */
    protected int compareByTime(long t1, long t2) {
        if(t1 >= t2) {
            return -1;
        } else {
            return 1;
        } 
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}

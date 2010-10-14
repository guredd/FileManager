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

    public static final int SORT_NAME = 0;
    public static final int SORT_SIZE = 1;
    public static final int SORT_TIME = 2;

    //TODO: parameterize locale
    private Collator collator = Collator.getInstance(new Locale("ru","RU"));
    private int mode = 0;

    public abstract int compare(Object o1, Object o2);
    
    protected int compareByName(String name1, String name2) {
        return collator.compare(name1, name2);
    }

    protected int compareBySize(long l1, long l2) {
        if(l1 > l2) {
            return 1;
        } else if(l1 < l2) {
            return -1;
        } else {
            return 0;
        }
    }

    protected int compareByTime(long t1, long t2) {
        if(t1 > t2) {
            return 1;
        } else if(t1 < t2) {
            return -1;
        } else {
            return 0;
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}

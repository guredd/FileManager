package ru.guredd.jbfilemanager.lister;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 14.10.2010
 * Time: 22:38:27
 * To change this template use File | Settings | File Templates.
 */
public class UnsupportedLister implements ILister {

    public IListedItem[] list(String type, String path) {
        IListedItem[] result = new IListedItem[1];
        result[0] = new SimpleItem("unsupported container",IListedItem.UNSUPPORTED);
        return result;  
    }

    public void setMode(int mode) {
        //nothing
    }
}

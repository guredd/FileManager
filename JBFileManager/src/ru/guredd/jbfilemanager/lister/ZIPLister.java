package ru.guredd.jbfilemanager.lister;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 11.10.2010
 * Time: 1:39:03
 * To change this template use File | Settings | File Templates.
 */
public class ZIPLister {
     public List<IListedItem> list(String path) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setMode(int mode) {
        if(mode != ILister.PREDEFINED_SIMPLE_MODE) {
            throw new UnsupportedOperationException("only PREDEFINED_SIMPLE_MODE(0) is supported");
        }
    }
}

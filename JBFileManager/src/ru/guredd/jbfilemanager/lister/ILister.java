package ru.guredd.jbfilemanager.lister;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 11.10.2010
 * Time: 0:03:01
 * To change this template use File | Settings | File Templates.
 */
public interface ILister {
    public static int PREDEFINED_SIMPLE_MODE = 0;
    public static int PREDEFINED_COMPLEX_MODE = 1;
        
    public List<IListedItem> list(String path);
    public void setMode(int mode);
}
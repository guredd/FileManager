package ru.guredd.jbfilemanager.lister;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 13.10.2010
 * Time: 20:42:57
 * To change this template use File | Settings | File Templates.
 */
public interface IListedItem {
    public static final String FOLDER = "folder";
    public String getName();
    public String getType();
    public boolean isExpandable();
}

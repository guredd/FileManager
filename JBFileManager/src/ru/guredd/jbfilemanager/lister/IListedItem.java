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
    public static final String UNKNOWN = "unknown";
    public static final String UNSUPPORTED = "unsupp";
    public static final String ERROR = "err";
    public String getName();
    public String getType();
    public boolean isExpandable();
}

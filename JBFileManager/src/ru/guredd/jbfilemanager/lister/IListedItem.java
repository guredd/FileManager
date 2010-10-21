package ru.guredd.jbfilemanager.lister;

/**
 * JBFileManager from Eduard Gurskiy, 2010
 *
 * Interface for listed item.
 */
public interface IListedItem {

    /**
     * Item type constant for folders.
     */
    public static final String FOLDER = "folder";
    /**
     * Item type constant for unknown types (no extension).
     */
    public static final String UNKNOWN = "unknown";
    /**
     * Item type constant for unsupported containers.
     */
    public static final String UNSUPPORTED = "unsupp";
    /**
     * Item type constant for error items.
     */
    public static final String ERROR = "err";

    /**
     * @return item name
     */
    public String getName();

    /**
     * @return item type
     */
    public String getType();

    /**
     * @return true if item is expandable(container), otherwise - false
     */
    public boolean isExpandable();
}

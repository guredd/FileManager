package ru.guredd.jbfilemanager.lister;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * <br>
 * JBFileManager
 * <br>
 * Item class for generic folder container.
 *
 * @author Eduard Gurskiy
 */
public class SimpleItem implements IListedItem {

    /**
     * item name
     */
    private String name = null;
    /**
     * item type
     */
    private String type = null;

    /**
     * Constructor.
     * @param name item name
     * @param type item type
     */
    public SimpleItem(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * @param name sets item name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param type sets item type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return item name
     */
    public String getName() {
        return name;
    }

    /**
     * @return item type
     */
    public String getType() {
        return type;
    }

    /**
     * @return true if item is expandable(container), otherwise - false
     */
    public boolean isExpandable() {
        return ListerLocator.hasType(type);
    }

    /**
     * Utility function which get item type by its name. File extension is extracted, if exists, otherwise IListedItem.UNKNOWN
     * @param name item(file) name
     * @return item type
     */
    public static String getTypeByName(String name) {
        if(name == null) {
            return IListedItem.UNKNOWN;
        } else {
            return (name.lastIndexOf(".")==-1)?IListedItem.UNKNOWN:name.substring(name.lastIndexOf(".")+1,name.length());
        }
    }
}

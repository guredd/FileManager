package ru.guredd.jbfilemanager.lister;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * @author Eduard Gurskiy
 *
 * JBFileManager
 *
 * Item class for ZIP container.
 */
public class ZIPSimpleItem extends SimpleItem {

    /**
     * Constructor.
     * @param name item name
     * @param type item type
     */
    public ZIPSimpleItem(String name, String type) {
        super(name, type);
    }

    /**
     * Folder flag.
     */
    private boolean isDirectory = false;

    /**
     * @return true if item is expandable(container), otherwise - false
     */
    @Override
    public boolean isExpandable() {
        return isDirectory();
    }

    /**
     * @return true if item is folder
     */
    public boolean isDirectory() {
        return isDirectory;
    }

    /**
     * @param directory sets folder flag
     */
    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }
}

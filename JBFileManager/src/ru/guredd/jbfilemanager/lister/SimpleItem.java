package ru.guredd.jbfilemanager.lister;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 13.10.2010
 * Time: 20:52:44
 * To change this template use File | Settings | File Templates.
 */
public class SimpleItem implements IListedItem {

    private String name = null;
    private String type = null;

    public SimpleItem(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isExpandable() {
        return ListerLocator.hasType(type);
    }

    public static String getTypeByName(String name) {
        if(name == null) {
            return IListedItem.UNKNOWN;
        } else {
            return (name.lastIndexOf(".")==-1)?IListedItem.UNKNOWN:name.substring(name.lastIndexOf(".")+1,name.length());
        }
    }
}

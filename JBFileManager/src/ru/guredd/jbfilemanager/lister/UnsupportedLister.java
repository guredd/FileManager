package ru.guredd.jbfilemanager.lister;

/**
 * JBFileManager from Eduard Gurskiy, 2010
 *
 * Unsupported containers lister. Returns always single item informing about unsupported container.
 */
public class UnsupportedLister implements ILister {

    /**
     * Lists unsupported container.
     * @param type container type
     * @param path container path
     * @return array of items inside specified container, will be always one item
     */
    public IListedItem[] list(String type, String path) {
        IListedItem[] result = new IListedItem[1];
        result[0] = new SimpleItem("unsupported container", IListedItem.UNSUPPORTED);
        return result;
    }

    /**
     * Nothing doing. For interface compatibility.
     * @param mode nothing
     */
    public void setMode(int mode) {
        //nothing
    }
}

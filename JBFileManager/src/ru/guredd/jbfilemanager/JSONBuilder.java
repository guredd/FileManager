package ru.guredd.jbfilemanager;

import ru.guredd.jbfilemanager.lister.IListedItem;

import javax.servlet.http.HttpSession;

/**
 * JBFileManager from Eduard Gurskiy, 2010
 *
 * JSON builder for listers results.
 */
public class JSONBuilder {

    /**
     * JSON nodes array name.
     */
    private static final String NODES = "nodes";
    /**
     * JSON node name attribute name.
     */
    private static final String NODE_NAME = "name";
    /**
     * JSON node type attribute name.
     */
    private static final String NODE_TYPE = "type";
    /**
     * JSON node expandability attribute name.
     */
    private static final String NODE_EXPANDABLE = "exp";
    /**
     * JSON node expanded status attribute name.
     */
    private static final String IS_EXPANDED = "isexp";

    /**
     * Builds JSON string for provided items list.
     * @param items list of items
     * @param path path of these items
     * @param session current HTTP session
     * @return JSON string with serialized items
     */
    public static String buildItemsList(IListedItem[] items, String path, HttpSession session) {
        StringBuffer buf = new StringBuffer("{\"" + NODES + "\":[");
        if(items != null) {
            for(int i=0;i<items.length;i++) {
                buf.append("{\"" + NODE_NAME + "\":\"");
                buf.append(items[i].getName());
                buf.append("\",\"" + NODE_TYPE + "\":\"");
                buf.append(items[i].getType());
                buf.append("\",\"" + NODE_EXPANDABLE + "\":\"");
                buf.append(items[i].isExpandable());               

                if(HttpSessionTreePersistence.isExpanded(path + '/' + items[i].getName(),session)) {
                    buf.append("\",\"" + IS_EXPANDED + "\":\"");
                    buf.append("true");
                }
                buf.append("\"}");

                if(i != items.length-1) {
                    buf.append(',');
                }
            }
        }
        buf.append("]}");
        return buf.toString();
    }
}

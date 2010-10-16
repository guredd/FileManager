package ru.guredd.jbfilemanager;

import ru.guredd.jbfilemanager.lister.IListedItem;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 15.10.2010
 * Time: 1:16:36
 * To change this template use File | Settings | File Templates.
 */
public class JSONBuilder {

    private static final String NODES = "nodes";
    private static final String NODE_NAME = "name";
    private static final String NODE_TYPE = "type";
    private static final String NODE_EXPANDABLE = "exp";

    public static String buildItemsList(IListedItem[] items) {
        StringBuffer buf = new StringBuffer("{\"" + NODES + "\":[");
        if(items != null) {
            for(int i=0;i<items.length;i++) {
                buf.append("{\"" + NODE_NAME + "\":\"");
                buf.append(items[i].getName());
                buf.append("\",\"" + NODE_TYPE + "\":\"");
                buf.append(items[i].getType());
                buf.append("\",\"" + NODE_EXPANDABLE + "\":\"");
                buf.append(items[i].isExpandable());
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

package ru.guredd.jbfilemanager;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 19.10.2010
 * Time: 16:42:44
 * To change this template use File | Settings | File Templates.
 */
public class HttpSessionTreePersistence {

    private static final String EXPANDED_NODES = "jbfm_expanded_nodes";

    private static List<String> createExpandedNodes(HttpSession session) {
        if(session != null) {
            List<String> expNodes = new ArrayList<String>();
            session.setAttribute(EXPANDED_NODES,expNodes);
            return expNodes;
        } else {
            return null;
        }
    }

    public static void addExpandedPath(String path, HttpSession session) {
        if(session == null) {
            return;
        }
        List<String> expNodes;
        Object obj;
        if((obj = session.getAttribute(EXPANDED_NODES)) == null || !(obj instanceof List)) {
            expNodes = createExpandedNodes(session);
        } else {
            expNodes = (List)obj;
        }
        if(expNodes != null && !expNodes.contains(path)) {
            expNodes.add(path);
        }        
    }

    public static void removeExpandedPath(String path, HttpSession session) {
        if(session == null) {
            return;
        }
        Object obj;
        if((obj = session.getAttribute(EXPANDED_NODES)) != null && obj instanceof List) {
            ((List) obj).remove(path);
        }
    }

    public static boolean isExpanded(String path, HttpSession session) {
        if(session == null) {
            return false;
        }
        Object obj;
        if((obj = session.getAttribute(EXPANDED_NODES)) != null && obj instanceof List) {
            return ((List) obj).contains(path);
        } else {
            return false;
        }
    }
}

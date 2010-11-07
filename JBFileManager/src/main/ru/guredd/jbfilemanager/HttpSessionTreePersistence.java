package ru.guredd.jbfilemanager;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * <br>
 * JBFileManager
 * <br>
 * Class responsible for persistence of JBFM tree state via HTTP session.
 *
 * @author Eduard Gurskiy
 */
public class HttpSessionTreePersistence {

    /**
     * Name of HTTP session attribute, where to store expanded path.
     */
    private static final String EXPANDED_NODES = "ru.guredd.jbfilemanager.HttpSessionTreePersistence:jbfm_expanded_nodes";

    /**
     * Creates array of expanded paths as HTTP session attribute.
     * @param session current HTTP session
     * @return array of expanded paths stored in HTTP session, if session is not null, otherwise - returns null
     */
    private static List<String> createExpandedNodes(HttpSession session) {
        if(session != null) {
            List<String> expNodes = new ArrayList<String>();
            session.setAttribute(EXPANDED_NODES,expNodes);
            return expNodes;
        } else {
            return null;
        }
    }

    /**
     * Adds expanded path to array stored in HTTP session.
     * @param path path to add
     * @param session current HTTP session
     */
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

    /**
     * Removes expanded path from array stored in HTTP session.
     * @param path path to remove
     * @param session current HTTP session
     */
    public static void removeExpandedPath(String path, HttpSession session) {
        if(session == null) {
            return;
        }
        Object obj;
        if((obj = session.getAttribute(EXPANDED_NODES)) != null && obj instanceof List) {
            ((List) obj).remove(path);
        }
    }

    /**
     * Checks if specified path was already expanded.
     * @param path path to check
     * @param session current HTTP session
     * @return true if path was already expanded during specified session, otherwise - false
     */
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

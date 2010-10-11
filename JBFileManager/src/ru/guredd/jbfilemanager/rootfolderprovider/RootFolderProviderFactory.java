package ru.guredd.jbfilemanager.rootfolderprovider;

import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 09.10.2010
 * Time: 22:50:54
 * To change this template use File | Settings | File Templates.
 */
public final class RootFolderProviderFactory {

    private static boolean initialized = false;
    private static IRootFolderProvider rootFolderProvider = null;

    public synchronized static IRootFolderProvider getRootFolderProvider() {
        if(!initialized) {
            return null;
        }
        return rootFolderProvider;
    }

    public synchronized static void initialize(String className, List objs) {
        initialized = false;
        rootFolderProvider = null;
        try {
            Object obj = Class.forName(className).newInstance();
            if(obj instanceof IRootFolderProvider) {
                rootFolderProvider = (IRootFolderProvider) obj;
                rootFolderProvider.initialize(objs);
                if(rootFolderProvider.isInitialized()) {
                    initialized = true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}

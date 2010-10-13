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
        if(initialized) {
            return rootFolderProvider;
        } else {
            return null;
        }
    }

    public synchronized static void initialize(String className, List objs) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        initialized = false;
        rootFolderProvider = null;
        Object obj = Class.forName(className).newInstance();
        if(obj instanceof IRootFolderProvider) {
            rootFolderProvider = (IRootFolderProvider) obj;
            rootFolderProvider.initialize(objs);
            if(rootFolderProvider.isInitialized()) {
                initialized = true;
            }
        } else {
            throw new InstantiationException("failed to instantiate IRootFolderProvider");
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}

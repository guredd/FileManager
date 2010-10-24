package ru.guredd.jbfilemanager.rootfolderprovider;

import java.util.List;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * @author Eduard Gurskiy
 *
 * JBFileManager
 *
 * Locator for root folder provider.
 */
public final class RootFolderProviderLocator {

    /**
     * Initialization flag.
     */
    private static boolean initialized = false;
    /**
     * Single root folder provider.
     */
    private static IRootFolderProvider rootFolderProvider = null;

    /**
     * Provides instance of root folder provider.
     * @return root folder provider
     */
    public synchronized static IRootFolderProvider getRootFolderProvider() {
        if(initialized) {
            return rootFolderProvider;
        } else {
            return null;
        }
    }

    /**
     * Instantiates anf initializes configured root folder provider.
     * @param className class name for root folder provider
     * @param objs configuration objects
     * @throws IllegalAccessException in case of error
     * @throws InstantiationException in case of error
     * @throws ClassNotFoundException in case of error
     */
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

    /**
     * Checks if locator was initialized.
     * @return true if initialized, otherwise false
     */
    public static boolean isInitialized() {
        return initialized;
    }
}

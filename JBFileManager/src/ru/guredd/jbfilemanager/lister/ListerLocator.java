package ru.guredd.jbfilemanager.lister;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * @author Eduard Gurskiy
 *
 * JBFileManager
 *
 * Locator for listers.
 */
public final class ListerLocator {

    /**
     * Maps container type to corresponding lister instance.
     */
    private static Map<String,ILister> listers = new HashMap<String,ILister>();

    /**
     * Initialization flag.
     */
    private static boolean initialized = false;

    /**
     * Initializes locator instantiating all configured listers (from properties file for listers).
     * @param props listers properties
     * @param mode listers detalisation mode
     * @throws IOException in case of errror
     * @throws ClassNotFoundException in case of errror
     * @throws IllegalAccessException in case of errror
     * @throws InstantiationException in case of errror
     */
    public synchronized static void initialize(Properties props, int mode) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        initialized = false;
        if (props != null) {
            Enumeration keys = props.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                Object obj = Class.forName(props.getProperty(key)).newInstance();
                if (obj instanceof ILister) {
                    ((ILister) obj).setMode(mode);
                    listers.put(key, (ILister) obj);
                } else {
                    throw new InstantiationException("failed to instantiate ILister");
                }
            }
            initialized = true;
        }
    }

    /**
     * Checks if specified type can be listed (corresponding lister is available).
     * @param type type to check
     * @return true if it can be listed, otherwise - false
     */
    public static boolean hasType(String type) {
        if(initialized) {
            return listers.containsKey(type);
        } else {
            return false;
        }
    }

    /**
     * Checks if locator is initialized.
     * @return true if initialized, otherwise - false
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Provides the lister instance for specified container type.
     * @param type container type
     * @return lister instance if available for specified type, otherwise - null
     */
    public static ILister getLister(String type) {
        if(initialized) {
            return listers.get(type);
        } else {
            return null;
        }
    }
}

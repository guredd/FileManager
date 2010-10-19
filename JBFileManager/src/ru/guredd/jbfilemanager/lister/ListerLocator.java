package ru.guredd.jbfilemanager.lister;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 13.10.2010
 * Time: 20:59:56
 * To change this template use File | Settings | File Templates.
 */
public final class ListerLocator {

    private static Map<String,ILister> listers = new HashMap<String,ILister>();
    private static boolean initialized = false;

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

    public static boolean hasType(String type) {
        if(initialized) {
            return listers.containsKey(type);
        } else {
            return false;
        }
    }

     public static boolean isInitialized() {
        return initialized;
    }

    public static ILister getLister(String type) {
        if(initialized) {
            return listers.get(type);
        } else {
            return null;
        }
    }
}

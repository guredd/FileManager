package ru.guredd.jbfilemanager.rootfolderprovider;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 09.10.2010
 * Time: 14:24:40
 */
public class FixedRootFolderProvider implements IRootFolderProvider {

    private static final String ROOT_PATH = "root_path";

    private boolean initialized = false;
    private File root = null;

    public void initialize(List objs) {        
        initialized = false;
        root = null;
        for(Object obj:objs) {
            if(obj instanceof ServletConfig) {
                String rootPath = ((ServletConfig) obj).getInitParameter(ROOT_PATH);
                if(rootPath != null) {
                    root = new File(rootPath);
                    if(root != null && root.isDirectory()) {
                        initialized = true;
                    }
                }
            }
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public File getRootFolder(HttpSession session) {
        if(!initialized)
            {return null;}
        else {
            return root;
        }
    }
}

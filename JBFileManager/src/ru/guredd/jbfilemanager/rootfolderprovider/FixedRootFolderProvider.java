package ru.guredd.jbfilemanager.rootfolderprovider;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

/**
 * JBFileManager from Eduard Gurskiy, 2010
 *
 * Static root folder provider. Returns always the same root folder, which is preconfigured in JBFM servlet.
 */
public class FixedRootFolderProvider implements IRootFolderProvider {

    /**
     * Name of configuration parameter for static root.
     */
    private static final String ROOT_PATH = "root_path";

    /**
     * Initialization flag.
     */
    private boolean initialized = false;
    /**
     * Root.
     */
    private File root = null;

    /**
     * Initializes root folder provider.
     * @param objs configuration objects (ServletConfig and ServletContext)
     */
    public void initialize(List objs) {        
        initialized = false;
        root = null;
        for(Object obj:objs) {
            if(obj instanceof ServletConfig) {
                String rootPath = ((ServletConfig) obj).getInitParameter(ROOT_PATH);
                if(rootPath != null) {
                    root = new File(rootPath);
                    if(root.isDirectory()) {
                        initialized = true;
                    }
                }
            }
        }
    }

    /**
     * Checks if root folder provider is initialized.
     * @return true if initialized, otherwise false
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Provides root folder.
     * @param session current HTTP session
     * @return root folder as File object
     */
    public File getRootFolder(HttpSession session) {
        if(!initialized)
            {return null;}
        else {
            return root;
        }
    }
}

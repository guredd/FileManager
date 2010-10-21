package ru.guredd.jbfilemanager.rootfolderprovider;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

/**
 * JBFileManager from Eduard Gurskiy, 2010
 *
 * Interface for root folder providers.
 */
public interface IRootFolderProvider {
    /**
     * Initializes root folder provider.
     * @param objs configuration objects (ServletConfig and ServletContext)
     */
    public void initialize(List objs);

    /**
     * Checks if root folder provider is initialized.
     * @return true if initialized, otherwise false
     */
    public boolean isInitialized();

    /**
     * Provides root folder.
     * @param session current HTTP session
     * @return root folder as File object
     */
    public File getRootFolder(HttpSession session);
}

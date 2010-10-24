package ru.guredd.jbfilemanager.rootfolderprovider;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * @author Eduard Gurskiy
 *
 * JBFileManager
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

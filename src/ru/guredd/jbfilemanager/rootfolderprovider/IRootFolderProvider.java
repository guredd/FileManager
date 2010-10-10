package ru.guredd.jbfilemanager.rootfolderprovider;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 09.10.2010
 * Time: 13:23:17
 * To change this template use File | Settings | File Templates.
 */
public interface IRootFolderProvider {
    public void initialize(List objs);
    public boolean isInitialized();
    public File getRootFolder();
}

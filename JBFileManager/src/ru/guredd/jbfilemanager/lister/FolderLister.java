package ru.guredd.jbfilemanager.lister;

import java.io.File;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 11.10.2010
 * Time: 1:36:45
 * To change this template use File | Settings | File Templates.
 */
public class FolderLister implements ILister {

    public List<IListedItem> list(String path) {

        File dir = new File(path);

        String[] filesInDir = dir.list();

        // sort the list of files (optional)
        // Arrays.sort(filesInDir);


        for ( int i=0; i<filesInDir.length; i++ )
        {

        }
        return null;
    }

    public void setMode(int mode) {
        if(mode != ILister.PREDEFINED_SIMPLE_MODE) {
            throw new UnsupportedOperationException("only PREDEFINED_SIMPLE_MODE(0) is supported");
        }
    }
}

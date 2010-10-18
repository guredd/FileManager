package ru.guredd.jbfilemanager.lister;

import java.io.File;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 11.10.2010
 * Time: 1:36:45
 * To change this template use File | Settings | File Templates.
 */
public class FolderLister implements ILister {

    public IListedItem[] list(String type, String path) throws IOException {
        if(path == null) {
            return null;
        }
        File dir = new File(path);
        File[] filesInDir = dir.listFiles();
        if (filesInDir != null) {
            TreeSet<File> set = new TreeSet<File>(DefaultFileComparator.getInstance());
            for(int i=0;i<filesInDir.length;i++) {
                set.add(filesInDir[i]);
            }
            return buildList(set);
        } else {
            IListedItem[] result = new SimpleItem[1];
            result[0] = new SimpleItem("server failed to list this dir",IListedItem.ERROR);
            return result;    
        }
    }

    public void setMode(int mode) {
        if(mode != ILister.PREDEFINED_SIMPLE_MODE) {
            throw new UnsupportedOperationException("only PREDEFINED_SIMPLE_MODE(0) is supported");
        }
    }

    private IListedItem[] buildList(TreeSet<File> set) {
        if(set == null) {
            return null;
        }
        Object[] array = set.toArray();
        IListedItem[] result = new SimpleItem[array.length];
        String type;
        File f;
        for(int i=0;i<array.length;i++) {
            f = (File)array[i];
            if(f.isDirectory()) {
                type = IListedItem.FOLDER;
            } else {
                type = SimpleItem.getTypeByName(f.getName());
            }
            result[i] = new SimpleItem(f.getName(),type);
        }
        return result;
    }
}

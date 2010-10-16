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

    public IListedItem[] list(String path) throws IOException {
        if(path == null) {
            return null;
        }
        File dir = new File(path);
        File[] filesInDir = dir.listFiles();
        TreeSet<File> set = new TreeSet<File>(DefaultFileComparator.getInstance());

        for(int i=0;i<filesInDir.length;i++) {
            set.add(filesInDir[i]);
        }
        return buildList(set);
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
        File[] array = (File[])set.toArray();
        IListedItem[] result = new SimpleItem[array.length];
        String type;
        for(int i=0;i<array.length;i++) {
            if(array[i].isDirectory()) {
                type = IListedItem.FOLDER;
            } else {
                type = SimpleItem.getTypeByName(array[i].getName());
            }
            result[i] = new SimpleItem(array[i].getName(),type);
        }
        return result;
    }
}

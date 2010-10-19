package ru.guredd.jbfilemanager.lister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 11.10.2010
 * Time: 1:39:03
 * To change this template use File | Settings | File Templates.
 */
public class ZIPLister extends FolderLister {

    public IListedItem[] list(String type, String path) throws IOException {
        if(path == null) {
            return null;
        }
        int depth;
        String arr[] = path.split('.' + type + '/');
        String outpath;
        String inpath="";
        if(arr.length == 1) {
            depth = 1;
            outpath = path;
        } else {
            depth = arr[1].split("/").length + 1;
            inpath = arr[1];
            outpath = arr[0] + '.' + type;
        }

        ZipInputStream zis = null;
        TreeSet<ZipEntry> set = null;

        try {
            zis = new ZipInputStream(new FileInputStream(outpath));
            set = new TreeSet<ZipEntry>(DefaultFileComparator.getInstance());
            ZipEntry ze = null;

            while ((ze = zis.getNextEntry()) != null) {
                if(ze.getName().split("/").length == depth && ze.getName().startsWith(inpath)) {
                    //ZipEntry zecopy = new ZipEntry(ze.getName());
                    //zecopy.setSize(ze.getSize());
                    //zecopy.setTime(ze.getTime());
                    //set.add(zecopy);
                    zis.closeEntry();
                    set.add(ze);
                }
            }            
        } finally {
            if(zis != null) {
                zis.close();
            }
        }
        return buildList(inpath,type,set);
    }

    private IListedItem[] buildList(String inpath, String ziptype, TreeSet<ZipEntry> set) {
        if(set == null) {
            return null;
        }
        Object[] array = set.toArray();
        IListedItem[] result = new SimpleItem[array.length];
        String name;
        String type;
        ZipEntry z;
        for(int i=0;i<array.length;i++) {
            z = (ZipEntry)array[i];
            name = z.getName();
            if(inpath != null && inpath.length() > 0) {
                name = name.replaceFirst(inpath,"");
            }
            if(name.startsWith("/")) {
                name = name.substring(1,name.length());
            }
            if(z.isDirectory()) {
                type = ziptype;
                name = name.substring(0,name.length()-1);
            } else {
                type = SimpleItem.getTypeByName(z.getName());
            }
            
            result[i] = new SimpleItem(name,type);
        }
        return result;
    }
}

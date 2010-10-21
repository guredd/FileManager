package ru.guredd.jbfilemanager.lister;

import java.io.IOException;

/**
 * JBFileManager from Eduard Gurskiy, 2010
 *
 * Interface for container listers.
 */
public interface ILister {

    /**
     * Constant for simple detalisation mode.
     */
    public static int PREDEFINED_SIMPLE_MODE = 0;
    /**
     * Constant for complex detalisation mode.
     */
    public static int PREDEFINED_COMPLEX_MODE = 1;

    /**
     * Lists specified container.
     * @param type container type
     * @param path container path
     * @return array of items inside specified container
     * @throws IOException in case of error
     */
    public IListedItem[] list(String type, String path) throws IOException;

    /**
     * Sets lister detalisation mode.
     * @param mode mode to set
     */
    public void setMode(int mode);
}

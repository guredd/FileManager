package ru.guredd.jbfilemanager;

import ru.guredd.jbfilemanager.rootfolderprovider.RootFolderProviderFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 08.10.2010
 * Time: 23:42:47
 * To change this template use File | Settings | File Templates.
 */
public class PrimaryServlet extends HttpServlet {

    private static final String TYPE_OF_OPERATION = "type";

    private static final String OPERATION_LIST_DIR = "list";
    private static final String OPERATION_GET_CSS = "css";
    private static final String OPERATION_GET_JS = "js";

    private static final String PATH_PARAM = "path";
    private static final String NAME_PARAM = "name";

    private static final String INIT_PARAM_ROOT_FOLDER_PROVIDER = "root_folder_provider";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String operation = req.getParameter(TYPE_OF_OPERATION);
        if(operation != null) {
            if(OPERATION_LIST_DIR.equals(operation)) {

                if(!RootFolderProviderFactory.isInitialized()) {
                    List initList = new ArrayList();
                    initList.add(getServletConfig());
                    initList.add(req.getSession());
                    initList.add(getServletContext());

                    RootFolderProviderFactory.initialize(getServletConfig().getInitParameter(INIT_PARAM_ROOT_FOLDER_PROVIDER),initList);

                    if(!RootFolderProviderFactory.isInitialized()) {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        return;
                    }
                }

            } else if (OPERATION_GET_CSS.equals(operation)) {

            } else if (OPERATION_GET_JS.equals(operation)) {

            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void handleListDir() {
        
    }

    private void handleGetCSS() {
        
    }

    private void handleGetJS() {

    }
}

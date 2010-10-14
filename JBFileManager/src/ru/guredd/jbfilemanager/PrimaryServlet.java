package ru.guredd.jbfilemanager;

import ru.guredd.jbfilemanager.lister.IListedItem;
import ru.guredd.jbfilemanager.lister.ListerFactory;
import ru.guredd.jbfilemanager.rootfolderprovider.RootFolderProviderFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    private static final String PARAM_PATH = "path";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_NAME = "name";

    private static final String INIT_PARAM_ROOT_FOLDER_PROVIDER = "root_folder_provider";
    private static final String INIT_PARAM_DETAILS_MODE = "details_mode";

    private static final String LISTERS_CONFIG = "listers.properties";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        List<Object> initList = new ArrayList<Object>();
        initList.add(getServletConfig());
        initList.add(getServletContext());
        try {
            RootFolderProviderFactory.initialize(getServletConfig().getInitParameter(INIT_PARAM_ROOT_FOLDER_PROVIDER), initList);
        } catch (Exception e) {
            log("error during root folder provider initializing", e);
            throw new ServletException("error during root folder provider initializing",e);
        }

        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(LISTERS_CONFIG);
            if (in == null) {
                log("can not load config file, stream is null");
                throw new ServletException("can not load config file, stream is null");
            }
            Properties props = new Properties();
            props.load(in);
            int mode = Integer.parseInt(getServletConfig().getInitParameter(INIT_PARAM_DETAILS_MODE));
            ListerFactory.initialize(props, mode);
        } catch (Exception e) {
            log("error during listers initializing", e);
            throw new ServletException("error during listers initializing",e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String operation = req.getParameter(TYPE_OF_OPERATION);
        if(operation != null) {
            if(OPERATION_LIST_DIR.equals(operation)) {
                handleListDir(req,resp);
            } else if (OPERATION_GET_CSS.equals(operation)) {
                handleGetCSS(req,resp);
            } else if (OPERATION_GET_JS.equals(operation)) {
                handleGetJS(req,resp);
            } else {
                log("incorrect operation requested = " + operation);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            log("incorrect request, " + TYPE_OF_OPERATION + " is not defined");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void handleListDir(HttpServletRequest req, HttpServletResponse resp) {
        if (RootFolderProviderFactory.isInitialized()) {
            if (ListerFactory.isInitialized()) {
                String path = req.getParameter(PARAM_PATH);
                if (path != null) {
                    File root = RootFolderProviderFactory.getRootFolderProvider().getRootFolder(req.getSession(true));
                    if (root != null) {
                        String type = req.getParameter(PARAM_TYPE);
                        if(type==null) {
                            type = IListedItem.FOLDER;
                        }
                        try {
                            IListedItem[] result = ListerFactory.getLister(type).list(root.getCanonicalPath()+path);
                            String response = JSONBuilder.buildItemsList(result);
                        } catch (IOException e) {
                            log("error, while listing path: " + path,e);
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    } else {
                        log("returned root is null");
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } else {
                    log("incorrect request, " + PARAM_PATH + " is not defined");
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                log("listers are not initialized");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            log("root folder provider is not initialized");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGetCSS(HttpServletRequest req, HttpServletResponse resp) {
        
    }

    private void handleGetJS(HttpServletRequest req, HttpServletResponse resp) {

    }
}

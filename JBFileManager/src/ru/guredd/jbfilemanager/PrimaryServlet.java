package ru.guredd.jbfilemanager;

import ru.guredd.jbfilemanager.lister.ListerFactory;
import ru.guredd.jbfilemanager.rootfolderprovider.RootFolderProviderFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    private static final String PATH_PARAM = "path";
    private static final String NAME_PARAM = "name";

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
        }

        try {
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(LISTERS_CONFIG);
            if (in == null) {
                throw new IOException("can not load config file, stream is null");
            }
            Properties props = new Properties();
            props.load(in);
            int mode = Integer.parseInt(getServletConfig().getInitParameter(INIT_PARAM_DETAILS_MODE));
            ListerFactory.initialize(props, mode);
        } catch (Exception e) {
            log("error during listers initializing", e);
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
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void handleListDir(HttpServletRequest req, HttpServletResponse resp) {
        if (RootFolderProviderFactory.isInitialized()) {

        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGetCSS(HttpServletRequest req, HttpServletResponse resp) {
        
    }

    private void handleGetJS(HttpServletRequest req, HttpServletResponse resp) {

    }
}

package ru.guredd.jbfilemanager;

import ru.guredd.jbfilemanager.lister.*;
import ru.guredd.jbfilemanager.rootfolderprovider.RootFolderProviderLocator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 08.10.2010
 * Time: 23:42:47
 * To change this template use File | Settings | File Templates.
 */
public class PrimaryServlet extends HttpServlet {

    private static final String TYPE_OF_OPERATION = "op";

    private static final String OPERATION_LIST_DIR = "list";
    private static final String OPERATION_UNLIST_DIR = "unlist";
    private static final String OPERATION_GET_CSS = "css";
    private static final String OPERATION_GET_JS = "js";

    private static final String PARAM_PATH = "path";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_NAME = "name";

    private static final String INIT_PARAM_ROOT_FOLDER_PROVIDER = "root_folder_provider";
    private static final String INIT_PARAM_DETAILS_MODE = "details_mode";
    private static final String INIT_PARAM_SORT_MODE = "sort_mode";

    private static final String LISTERS_CONFIG = "listers.properties";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        List<Object> initList = new ArrayList<Object>();
        initList.add(getServletConfig());
        initList.add(getServletContext());
        try {
            RootFolderProviderLocator.initialize(getServletConfig().getInitParameter(INIT_PARAM_ROOT_FOLDER_PROVIDER), initList);
        } catch (Exception e) {
            log("error during root folder provider initializing", e);
            throw new ServletException("error during root folder provider initializing",e);
        }

        //TODO: File comparator class could be moved to servlet config as well
        String sort_mode = getServletConfig().getInitParameter(INIT_PARAM_SORT_MODE);
        if(sort_mode != null) {
            DefaultFileComparator.getInstance().setMode(sort_mode);
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
            ListerLocator.initialize(props, mode);
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
            } else if (OPERATION_UNLIST_DIR.equals(operation)) {
                handleUnListDir(req,resp);
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
        if (RootFolderProviderLocator.isInitialized()) {
            if (ListerLocator.isInitialized()) {
                String path = req.getParameter(PARAM_PATH);
                if (path != null) {
                    File root = RootFolderProviderLocator.getRootFolderProvider().getRootFolder(req.getSession());
                    if (root != null) {
                        String type = req.getParameter(PARAM_TYPE);
                        if(type==null) {
                            type = IListedItem.FOLDER;
                        }
                        try {
                            String pathDecoded = URLDecoder.decode(path,"UTF-8");                        
                            ILister lister = ListerLocator.getLister(type.toLowerCase());
                            if(lister != null) {

                                IListedItem[] result = lister.list(type,root.getCanonicalPath()+pathDecoded);
                                String jsonresponse = JSONBuilder.buildItemsList(result,pathDecoded,req.getSession());
                                HttpSessionTreePersistence.addExpandedPath(pathDecoded,req.getSession(true));

                                if(jsonresponse != null) {
                                    sendResponse(jsonresponse,HttpServletResponse.SC_OK,resp);
                                } else {
                                    sendErrorResponse("JSON response is null",null,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
                                }
                            } else {
                                sendErrorResponse("lister is not defined for type: " + type.toLowerCase(),null,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
                            }
                        } catch (Exception e) {
                            sendErrorResponse("error, while listing path",e,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
                        }
                    } else {
                        sendErrorResponse("returned root is null",null,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
                    }
                } else {
                    sendErrorResponse("incorrect request, " + PARAM_PATH + " is not defined",null,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
                }
            } else {
                sendErrorResponse("listers are not initialized",null,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
            }
        } else {
            sendErrorResponse("root folder provider is not initialized",null,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
        }
    }

    private void handleUnListDir(HttpServletRequest req, HttpServletResponse resp) {
        String path = req.getParameter(PARAM_PATH);
        if (path != null) {
            try {
                HttpSessionTreePersistence.removeExpandedPath(URLDecoder.decode(path,"UTF-8"),req.getSession());
            } catch (UnsupportedEncodingException e) {
                log("failed decoding URL",e);
            }
        }
        sendResponse("OK",HttpServletResponse.SC_OK,resp);        
    }

    private void handleGetCSS(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/css");
        String name = req.getParameter(PARAM_NAME);


    }


    private void handleGetJS(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter(PARAM_NAME);
        if (name != null) {
            resp.setContentType("text/javascript");
            resp.setHeader("Cache-Control", "public");
            resp.setStatus(HttpServletResponse.SC_OK);
            try {
                InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("/js/" + name);
                int val;
                while ((val = in.read()) != -1) {
                    resp.getOutputStream().write(val);
                }
                 resp.flushBuffer();
            }
            catch (IOException e) {
                log("error occurred, while sending response",e);
            }
        }
    }

    private void sendErrorResponse(String response, Throwable e,int code, HttpServletResponse resp) {
        log(response,e);
        sendResponse(response,code,resp);
    }

    private void sendResponse(String response,int code, HttpServletResponse resp) {
        resp.setStatus(code);
        resp.setContentType("text/html;charset=UTF-8");
        try {
            resp.getOutputStream().write(response.getBytes("UTF-8"));       
            resp.flushBuffer();
        } catch (IOException e) {
            log("error occurred, while sending response",e);
        }
    }

    private void sendFile(HttpServletRequest req, HttpServletResponse resp, String path) {

    }
}

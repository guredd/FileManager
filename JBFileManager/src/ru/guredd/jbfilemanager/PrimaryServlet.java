package ru.guredd.jbfilemanager;

import ru.guredd.jbfilemanager.lister.*;
import ru.guredd.jbfilemanager.rootfolderprovider.RootFolderProviderLocator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * @author Eduard Gurskiy
 *
 * JBFileManager
 *
 * Main Servlet class.
 */
public class PrimaryServlet extends HttpServlet {

    /**
     * Name of HTTP parameter for operation type.
     */
    private static final String TYPE_OF_OPERATION = "op";

    /**
     * Name of HTTP parameter value for list operation.
     */
    private static final String OPERATION_LIST_DIR = "list";
    /**
     * Name of HTTP parameter value for unlist operation.
     */
    private static final String OPERATION_UNLIST_DIR = "unlist";
    /**
     * Name of HTTP parameter value for css operation.
     */
    private static final String OPERATION_GET_CSS = "css";
    /**
     * Name of HTTP parameter value for js operation.
     */
    private static final String OPERATION_GET_JS = "js";
    /**
     * Name of HTTP parameter value for img operation.
     */
    private static final String OPERATION_GET_IMG = "img";

    /**
     * Name of HTTP parameter for path argument.
     */
    private static final String PARAM_PATH = "path";
    /**
     * Name of HTTP parameter for type argument.
     */
    private static final String PARAM_TYPE = "type";
    /**
     * Name of HTTP parameter for file name argument.
     */
    private static final String PARAM_NAME = "name";

    /**
     * Name of servlet config parameter for root folder provider.
     */
    private static final String INIT_PARAM_ROOT_FOLDER_PROVIDER = "root_folder_provider";
    /**
     * Name of servlet config parameter for details mode.
     */
    private static final String INIT_PARAM_DETAILS_MODE = "details_mode";
    /**
     * Name of servlet config parameter for sort mode.
     */
    private static final String INIT_PARAM_SORT_MODE = "sort_mode";
    /**
     * Name of listers configuration file.
     */
    private static final String LISTERS_CONFIG = "listers.properties";
    /**
     * Name of runtime replacement string in CSS for servlet URL.
     */
    private static final String CSS_URL_REPLACEMENT = "#JBFM_URL#";

    /**
     * Initializes servlet. 
     * @param config configuration object
     * @throws ServletException
     */
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

    /**
     * Dispatches GET request.
     * @param req request object
     * @param resp response object
     * @throws ServletException
     * @throws IOException
     */
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
            } else if (OPERATION_GET_IMG.equals(operation)) {
                handleGetImage(req,resp);
            } else {
                log("incorrect operation requested = " + operation);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            log("incorrect request, " + TYPE_OF_OPERATION + " is not defined");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Handles list operation.
     * @param req request object
     * @param resp response object
     */
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

    /**
     * Handles unlist operation.
     * @param req request object
     * @param resp response object
     */
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

    /**
     * Handles css operation. Substitutes servlet URL for resources location in runtime.
     * @param req request object
     * @param resp response object
     */
    private void handleGetCSS(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter(PARAM_NAME);
        if (name != null) {
            try {
                InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("css/" + name);
                if (in != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer fileData = new StringBuffer(1000);
                    char[] buf = new char[1024];
                    int numRead;
                    while ((numRead = reader.read(buf)) != -1) {
                        String readData = String.valueOf(buf, 0, numRead);
                        fileData.append(readData);
                        buf = new char[1024];
                    }
                    reader.close();
                    String cssString = fileData.toString();
                    if (cssString != null) {
                        String cssReplaced = cssString.replaceAll(CSS_URL_REPLACEMENT, req.getRequestURL().toString());
                        resp.getOutputStream().write(cssReplaced.getBytes("UTF-8"));
                        resp.flushBuffer();
                    }
                } else {
                    sendErrorResponse("failed to read css", null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, resp);
                }
            } catch (IOException e) {
                log("error occurred, while sending response", e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            sendErrorResponse("file name was not set", null, HttpServletResponse.SC_BAD_REQUEST, resp);
        }
    }

    /**
     * Handles js operation.
     * @param req request object
     * @param resp response object
     */
    private void handleGetJS(HttpServletRequest req, HttpServletResponse resp) {
        sendFile(req,resp,"js/","text/javascript");
    }

    /**
     * Handles img operation. Defines content type by file extension (last 3 letters).
     * @param req request object
     * @param resp response object
     */
    private void handleGetImage(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter(PARAM_NAME);
        if(name != null) {
            sendFile(req,resp,"img/","image/"+name.substring(name.length()-3,name.length()));
        } else {
             sendErrorResponse("file name was not set",null,HttpServletResponse.SC_BAD_REQUEST,resp);
        }
    }

    /**
     * Sends file from JBFM jar archive in response.
     * @param req request object
     * @param resp response object
     * @param folder folder name inside JBFM jar archive
     * @param contentType HTTP content type of the file
     */
    private void sendFile(HttpServletRequest req, HttpServletResponse resp,String folder,String contentType) {
        String name = req.getParameter(PARAM_NAME);
        if (name != null) {
            try {
                InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(folder + name);
                if(in != null) {
                    resp.setContentType(contentType);
                    resp.setHeader("Cache-Control", "public");
                    resp.setStatus(HttpServletResponse.SC_OK);
                    int val;
                    while ((val = in.read()) != -1) {
                        resp.getOutputStream().write(val);
                    }
                    resp.flushBuffer();
                } else {
                    sendErrorResponse("failed to read file",null,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,resp);
                }
            } catch (IOException e) {
                log("error occurred, while sending response",e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
             sendErrorResponse("file name was not set",null,HttpServletResponse.SC_BAD_REQUEST,resp);
        }
    }

    /**
     * Sends error response.
     * @param response error text
     * @param e throwable error
     * @param code error code
     * @see HttpServletResponse
     * @param resp response object
     */
    private void sendErrorResponse(String response, Throwable e,int code, HttpServletResponse resp) {
        log(response,e);
        sendResponse(response,code,resp);
    }

    /**
     * Sends text response.
     * @param response response text
     * @param code code
     * @see HttpServletResponse
     * @param resp response object
     */
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
}

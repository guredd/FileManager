package ru.guredd.jbfilemanager;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.apache.cactus.WebResponse;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * Copyright 2010 Eduard Gurskiy. All rights reserved.
 * License BSD. Use is subject to license terms.
 * <br>
 * JBFileManager
 * <br>
 * Tests for JBFM servlet.
 *
 * @author Eduard Gurskiy
 */
public class TestPrimaryServlet extends ServletTestCase {

    private PrimaryServlet servlet = null;

    private String sessionId = null;

    private static String CACTUS_CONTEXT_URL = "cactus.contextURL";
    private static String TOMCAT_SESSION_COOKIE = "JSESSIONID";

    public TestPrimaryServlet(String theName) {
        super(theName);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestPrimaryServlet("testListRoot"));
        suite.addTest(new TestPrimaryServlet("testRandomListing"));
        suite.addTest(new TestPrimaryServlet("testUnlist"));
        suite.addTest(new TestPrimaryServlet("testPersistence"));        
        return suite;
    }

    @Override
    protected void setUp() throws Exception {
        if(servlet == null) {
            servlet = new PrimaryServlet();
            config.setInitParameter("root_folder_provider","ru.guredd.jbfilemanager.rootfolderprovider.FixedRootFolderProvider");
            config.setInitParameter("root_path","C:\\");
            config.setInitParameter("details_mode","0");
            config.setInitParameter("sort_mode","name");
            servlet.init(config);
        }
    }

    //-------------------------------------------------------------------------
    // ListRoot
    // Tests listing of root node.

    public void beginListRoot(WebRequest theRequest)
    {
        theRequest.addParameter("op","list");
        theRequest.addParameter("path","/");
    }

    public void testListRoot() throws IOException, ServletException {
        if(servlet != null) {
            servlet.doGet(request,response);
        } else {
            fail("Servlet is null");
        }
    }

    public void endListRoot(WebResponse theResponse)
    {
        assertTrue(theResponse.getStatusCode() == HttpServletResponse.SC_OK);
        String result = theResponse.getText();
        JSONArray jsonArr = parseListResponse(result);
        assertTrue("Empty result",jsonArr.size() > 0);
    }

    //------------------------------------------------------------------------- 
    // RandomListing
    // Tests list operation of single random expandable node on first level.

    public void beginRandomListing(WebRequest theRequest) throws IOException {

        JSONArray jsonArr = performList("/");
        theRequest.addParameter("op","list");
        theRequest.addParameter("path","/" + selectRandomExpandable(jsonArr));
    }

    public void testRandomListing() throws IOException, ServletException {
        if(servlet != null) {
            servlet.doGet(request,response);
        } else {
            fail("Servlet is null");
        }        
    }

    public void endRandomListing(WebResponse theResponse)
    {
        assertTrue(theResponse.getStatusCode() == HttpServletResponse.SC_OK);
        String result = theResponse.getText();
        parseListResponse(result);
    }

    //-------------------------------------------------------------------------
    // Unlist
    // Tests unlist operation of single random expandable node on first level.

    public void beginUnlist(WebRequest theRequest) throws IOException {

        JSONArray jsonArr = performList("/");
        String node = selectRandomExpandable(jsonArr);
        performList("/" + node);
        assertNotNull("Session id is null",sessionId);
        theRequest.addCookie(TOMCAT_SESSION_COOKIE,sessionId);
        theRequest.addParameter("op","unlist");
        theRequest.addParameter("path","/" + node);
    }

    public void testUnlist() throws IOException, ServletException {
        if(servlet != null) {
            servlet.doGet(request,response);
        } else {
            fail("Servlet is null");
        }
    }

    public void endUnlist(WebResponse theResponse)
    {
        assertTrue(theResponse.getStatusCode() == HttpServletResponse.SC_OK);
    }

    //-------------------------------------------------------------------------
    // Persistence
    // Tests persistence mechanism. Performs listing of random expandable node,
    // then performs listing of root node (as if F5 was pressed in browser).
    // Validates that one node is marked as expanded.    

    public void beginPersistence(WebRequest theRequest) throws IOException {

        JSONArray jsonArr = performList("/");
        String node = selectRandomExpandable(jsonArr);
        performList("/" + node);
        assertNotNull("Session id is null",sessionId);        
        theRequest.addCookie(TOMCAT_SESSION_COOKIE,sessionId);
        theRequest.addParameter("op","list");
        theRequest.addParameter("path","/");
    }

    public void testPersistence() throws IOException, ServletException {
        if(servlet != null) {
            servlet.doGet(request,response);
        } else {
            fail("Servlet is null");
        }
    }

    public void endPersistence(WebResponse theResponse)
    {
        assertTrue(theResponse.getStatusCode() == HttpServletResponse.SC_OK);
        String result = theResponse.getText();
        JSONArray jsonArr = parseListResponse(result);
        assertTrue("Empty result",jsonArr.size() > 0);
        for(Object obj:jsonArr) {
            if(((JSONObject) obj).size() == 4) {
                return;
            }
        }
        fail("Expanded node was not marked in response");
    }

    //-----------------------------UTILITY FUNCTIONS--------------------------------------------

    /**
     * Performs list operation for tests scenarios.
     * @param path path to list
     * @return array of listed nodes
     * @throws IOException
     */
    private JSONArray performList(String path) throws IOException {
        HttpClient client;
        GetMethod method = null;
        String response = null;
        try {
            client = new HttpClient();
            method = new GetMethod(System.getProperty(CACTUS_CONTEXT_URL) + "/jbfm");
            method.setQueryString(new NameValuePair[]{new NameValuePair("op", "list"), new NameValuePair("path", path)});
            if(sessionId != null) {
                method.addRequestHeader("Cookie", TOMCAT_SESSION_COOKIE + "=" + sessionId);
            }
            assertTrue(client.executeMethod(method) == HttpServletResponse.SC_OK);
            response = method.getResponseBodyAsString();
            retrieveSessionId(method);
        } finally {
            method.releaseConnection();
        }
        return parseListResponse(response);
    }

    /**
     * Parses response of list operation.
     * @param response HTTP response of list operation
     * @return array of listed nodes
     */
    private JSONArray parseListResponse(String response) {
        assertNotNull("Response string is null",response);
        Object json  = JSONValue.parse(response);
        assertFalse("Invalid data received: " + response,json == null || !(json instanceof JSONObject));
        Object arr = ((JSONObject)json).get("nodes");
        assertFalse("Invalid data received: " + response,arr == null || !(arr instanceof JSONArray));
        return (JSONArray)arr;
    }

    /**
     * Selects random expandable node from array of listed nodes.
     * Assumes that expandable nodes are at the beginning of array.
     * @param jsonArr array of listed nodes
     * @return name of selected expandable node
     */
    private String selectRandomExpandable(JSONArray jsonArr) {
        int countExpandable = 0;
        for(Object obj:jsonArr) {
            assertTrue("Invalid data received: " + obj,obj instanceof JSONObject);
            JSONObject elem = (JSONObject) obj;
            if("true".equals(elem.get("exp"))) {
                countExpandable++;
            } else {
                break;
            }
        }
        assertTrue("Can't perform test, no expandable nodes found",countExpandable > 0);
        Random random = new Random();
        int rand = random.nextInt(countExpandable);
        return (String)((JSONObject)jsonArr.get(rand)).get("name");
    }

    /**
     * Retrieves and saves session id from HTTP response.
     * @param method executed HTTP GET method
     */
    private void retrieveSessionId(GetMethod method) {
        Header header = method.getResponseHeader("Set-Cookie");
        if (header != null) {
            for (HeaderElement elem : header.getElements()) {
                if (TOMCAT_SESSION_COOKIE.equals(elem.getName().toUpperCase())) {
                    sessionId = elem.getValue();
                    break;
                }
            }
        }
    }
}

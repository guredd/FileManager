<%--
  Created by IntelliJ IDEA.
  User: guredd
  Date: 09.10.2010
  Time: 13:44:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
      <!-- provide link to jbfm.css
      URL is: <JBFM servlet URL>?op=css&name=jbfm.css -->
      <link rel="stylesheet" href="http://localhost:8080/test/jbfm?op=css&name=jbfm.css" type="text/css">

      <!-- provide link to jquery, if it is not done before
      URL is: <JBFM servlet URL>?op=js&name=jquery-1.4.2.min.js -->
      <script src="http://localhost:8080/test/jbfm?op=js&name=jquery-1.4.2.min.js" type="text/javascript"></script>

      <!-- provide link to jquery, if it is not done before
      URL is: <JBFM servlet URL>?op=js&name=jbfm.js -->
      <script src="http://localhost:8080/test/jbfm?op=js&name=jbfm.js" type="text/javascript"></script>

      <title>JBFileManager test page</title>
  </head>
  <body>
    <p>Something before</p>

    <!-- define your UL with id -->
    <ul id="fm1"/>

    <!-- call jbfilemanager() function and provide: id of UL, URL of JBFM servlet, root label for UL -->
    <script>jbfilemanager("fm1","http://localhost:8080/test/jbfm","Root")</script>


    <p>Something after</p>
  </body>
</html>
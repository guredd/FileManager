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
      <script src="http://localhost:8080/test/jquery-1.4.2.min.js" type="text/javascript"></script>
      <script src="http://localhost:8080/test/jbfm.js" type="text/javascript"></script>      
      <title>JBFileManager test page</title></head>
  <body>
    <p>Something before</p>

    <ul id="fm1"/>
    <script>jbfilemanager("fm1","http://localhost:8080/JBFileManager","Files")</script>


    <p>Something after</p>
  </body>
</html>
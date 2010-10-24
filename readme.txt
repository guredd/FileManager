Copyright 2010 Eduard Gurskiy. All rights reserved.
License BSD. Use is subject to license terms.

JetBrains File Manager (JBFileManager or JBFM) made by Eduard Gurskiy as a test task for interview in JetBrains company.

-------------------------
Installation notes:

Distribution consists of 2 files:

JBFileManager.jar - file manager itself implemented as java servlet
TestWebApp.war - sample web application with index.jsp, where sample file manager control is located

To install JBFileManager perform the following steps:

1. Deploy JBFileManager servlet: 
	a. Put JBFileManager.jar somewhere(for example in WEB-INF/lib of your web application) to be accessible by your servlet container.
	b. Register JBFileManager servlet in your web application and configure it with right parameters: root folder provider, root path, details mode, sort mode.
	See web.xml in TestWebApp.war for details.

2. On each page, where JBFileManager will be used, make sure, that the following resources are available:
	a. JBFileManager CSS file (jbfm.css), can be referenced like this:
		<link rel="stylesheet" href="http://localhost:8080/test/jbfm?op=css&name=jbfm.css" type="text/css">
		where URL to CSS is: <JBFM servlet URL>?op=css&name=jbfm.css
		Alternatively, you can extract jbfm.css from JBFileManager.jar and put it anywhere you want.
	b. JQuery (1.4.2 or newer) javascript file (jquery-1.4.2.min.js), can be referenced like this: 
		<script src="http://localhost:8080/test/jbfm?op=js&name=jquery-1.4.2.min.js" type="text/javascript"></script>
		where URL to javascript is: <JBFM servlet URL>?op=js&name=jquery-1.4.2.min.js
		Alternatively, you can load JQuery javascript from any other location.
	c. JBFileManager javascript file (jbfm.js), can be referenced like this:
		<script src="http://localhost:8080/test/jbfm?op=js&name=jbfm.js" type="text/javascript"></script>
		where URL to javascript is: <JBFM servlet URL>?op=js&name=jbfm.js
		Alternatively, you can extract jbfm.js from JBFileManager.jar and put it anywhere you want.

3. Attach JBFileManager for any <UL> on you page by calling jbfilemanager(...) function with parameters: id of <UL>, JBFileManager servlet URL (see step 1), root label of your tree.	
	See index.jsp in TestWebApp.war for details.
	 
4. Enjoy! :)      

--------------------------
Customization points:

1. Add CSS (most common - icon) for any custom file extension, by modifying jbfm.css and adding additional resources in /img subfolder of JBFileManager.jar
   See jbfm.css in JBFileManager.jar for details.

2. Add or remove available container listers by editing listers.properties file of JBFileManager.jar.
   Additional listers could be created and added (implement ru.guredd.jbfilemanager.lister.ILister, add it to JBFileManager.jar and register listers.properties by container file extension).
   See javadocs for details.

3. Different root folder provider could be created and added (implement ru.guredd.jbfilemanager.rootfolderprovider.IRootFolderProvider, add it to JBFileManager.jar and register in servlet configuration).
   See javadocs for details.   

4. Sources are open, so you can do whatever you want to make it better. :)

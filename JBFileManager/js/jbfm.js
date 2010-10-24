/* jbfm.js - JS file for JBFileManager from Eduard Gurskiy, 2010 */

function jbfilemanager(id, url, rootLabel) {

    // show root node:
    var rootUL = $('#'+id);
    rootUL.addClass('jbfm-container');
    var rootLI = $('<LI>');
    rootLI.addClass('jbfm-node jbfm-root jbfm-last jbfm-closed');
    rootLI.html('<div class="jbfm-expand"></div>' +
                '<div class="jbfm-type-folder"></div>' +
                '<div class="jbfm-name">' + rootLabel + '</div>' +
                '<ul class="jbfm-container"></ul>');
    rootLI.data("type","folder");
    rootUL.append(rootLI);

    // switch between expanded/collapsed states:
    function switchNodeClass(node) {
        if(node.hasClass('jbfm-opened')) {
            node.toggleClass('jbfm-opened',false);
            node.toggleClass('jbfm-closed',true);
            $(node.children('UL:first')[0]).empty();
        } else {
            node.toggleClass('jbfm-closed',false);
            node.toggleClass('jbfm-opened',true);
        }
    }

    // build node path as string for ajax request:
    function buildNodePath(node) {
        var inode = node;
        var path = '';
        while (!inode.hasClass('jbfm-root')) {
            path = '/' + $(inode.children('DIV:eq(2)')[0]).text() + path;
            inode = $(inode.parent().parent());
        }
        return path;
    }

    // list node contents:
    function listNode(node) {

        // ajax successful callback:
        var onSuccess = function(data) {
            onLoaded(data.nodes);
            showProgress(false);
        };

        // ajax error callback:
        var onError = function(xhr, status) {
            showProgress(false);
            var text;
            if (xhr.status != 200) {
                text = xhr.responseText;
            } else {
                text = 'Incorrect data received!';
            }
            var msg = "Error " + status  + ' : ' + text;
            alert(msg)
        };

        // show/hide animated progress:
        function showProgress(on) {
            var expand = $(node.children('div:first')[0]);
            expand.toggleClass( !on ? 'jbfm-progress' : 'jbfm-expand',false);
            expand.toggleClass( on ? 'jbfm-progress' : 'jbfm-expand',true);                   
        }

        // render node contents from JSON result:
        function onLoaded(nodes) {

            for(var i=0; i<nodes.length; i++) {
                var child = nodes[i];
                var li = $('<LI>');

                li.toggleClass('jbfm-node');
                li.toggleClass(child.exp == 'true' ? 'jbfm-closed' : 'jbfm-leaf');

                if (i == nodes.length-1) {
                    li.toggleClass('jbfm-last');
                }

                var type = child.type.toLowerCase();

                li.html('<div class="jbfm-expand"></div>' +
                        '<div class="jbfm-type-default jbfm-type-' + type + '"></div>' +
                        '<div class="jbfm-name">'+child.name+'</div>');

                li.data("type",type);

                if (child.exp == "true") {
                    li.html(li.html() + '<ul class="jbfm-container"></ul>');
                }
                $(node.children('UL:first')[0]).append(li);

                if(child.isexp == "true") {
                    listNode(li);
                }
            }            
            switchNodeClass(node);
        }      

        showProgress(true);

        // request node contents from server:
        $.ajax({
            url: url,
            data: {op:"list",type:node.data("type"),path:encodeURIComponent(buildNodePath(node))},
            dataType: "json",
            success: onSuccess,
            error: onError,
            cache: false
        });
    }

    // unlist node contents:
    function unlistNode(node) {
        switchNodeClass(node);

        $.ajax({
            url: url,
            data: {op:"unlist",path:encodeURIComponent(buildNodePath(node))},
            dataType: "json",
            cache: false
        });
    }

    //register onclick callback for whole tree:
    rootUL.click(function(event) {
        event = event || window.event;
        var clickedElem = event.target || event.srcElement;

        if (!$(clickedElem).hasClass('jbfm-expand')) {
            return;
        }

        var node = $(clickedElem.parentNode);
        if (node.hasClass('jbfm-leaf')) {
            return;
        }

        if (node.find('LI').length) {            
            unlistNode(node);
		} else {
            listNode(node);
        }
    });

    //list root node after initialization:
    listNode(rootLI);
}
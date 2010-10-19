/* jbfm.js - JS file for JBFileManager from Eduard Gurskiy, 2010 */

function jbfilemanager(id, url, rootLabel) {

    // show root node:
    var rootUL = document.getElementById(id)    
    $(rootUL).addClass('jbfm-container')
    var rootLI = document.createElement('LI')
    $(rootLI).addClass('jbfm-node jbfm-root jbfm-last jbfm-closed')
    rootLI.innerHTML = '<div class="jbfm-expand"></div>'
    rootLI.innerHTML += '<div class="jbfm-type-folder"></div>'
    rootLI.innerHTML += '<div class="jbfm-name">' + rootLabel + '</div>'
    rootLI.innerHTML += '<ul class="jbfm-container"></ul>'
    rootUL.appendChild(rootLI)   

    // switch between expanded/collapsed states:
    function switchNodeClass(node) {
        if($(node).hasClass('jbfm-opened')) {
            $(node).removeClass('jbfm-opened')
            $(node).addClass('jbfm-closed')
            node.getElementsByTagName('UL')[0].innerHTML = ''
        } else {
            $(node).removeClass('jbfm-closed')
            $(node).addClass('jbfm-opened')
        }
    }

    // build node path as string for ajax request:
    function buildNodePath(node) {
        var inode = node
        var path = ''
        while (!$(inode).hasClass('jbfm-root')) {
            path = '/' + inode.getElementsByTagName('DIV')[2].innerHTML + path
            inode = inode.parentNode.parentNode
        }
        return path
    }

    // list node contents:
    function listNode(node) {

        // ajax successful callback:
        var onSuccess = function(data) {
            onLoaded(data.nodes)
            showProgress(false)
        }

        // ajax error callback:
        var onError = function(xhr, status) {
            showProgress(false)
            var text
            if (xhr.status != 200) {
                text = xhr.responseText
            } else {
                text = 'Incorrect data received!'
            }
            var msg = "Error " + status  + ' : ' + text
            alert(msg)
        }

        // show/hide animated progress:
        function showProgress(on) {
            var expand = node.getElementsByTagName('DIV')[0]
            expand.className = on ? 'jbfm-progress' : 'jbfm-expand'
        }

        // render node contents from JSON result:
        function onLoaded(nodes) {

            for(var i=0; i<nodes.length; i++) {
                var child = nodes[i]
                var li = document.createElement('LI')

                $(li).addClass('jbfm-node')

                if(child.exp == "true") {
                     $(li).addClass('jbfm-closed')
                } else {
                     $(li).addClass('jbfm-leaf')
                }

                if (i == nodes.length-1) $(li).addClass('jbfm-last')

                li.innerHTML = '<div class="jbfm-expand"></div>'
                li.innerHTML += '<div class="jbfm-type-default jbfm-type-' + child.type.toLowerCase() + '"></div>'
                li.innerHTML += '<div class="jbfm-name">'+child.name+'</div>'          

                if (child.exp == "true") {
                    li.innerHTML += '<ul class="jbfm-container"></ul>'
                }
                node.getElementsByTagName('UL')[0].appendChild(li)

                if(child.isexp == "true") {
                    listNode(li)
                }
            }            
            switchNodeClass(node)
        }

        // get node type from associated jbfm-type-... class:
        function getNodeType() {
            var classes = node.getElementsByTagName('DIV')[1].className
            var arr = classes.split(' ',5)
            var clazz = ''
            for(var i=0; i<arr.length; i++) {
                if(arr[i].substr(0,10) == 'jbfm-type-' && arr[i] != 'jbfm-type-default') {
                    clazz = arr[i]
                    break
                }
            }
            return clazz.substr(10,clazz.length-10)
        }

        showProgress(true)

        // request node contents from server:
        $.ajax({
            url: url,
            data: {op:"list",type:getNodeType(),path:encodeURIComponent(buildNodePath(node))},
            dataType: "json",
            success: onSuccess,
            error: onError,
            cache: false
        })
    }

    //register onclick callback for whole tree:
    $(rootUL).click(function(event) {
        event = event || window.event
        var clickedElem = event.target || event.srcElement

        if (!$(clickedElem).hasClass('jbfm-expand')) {
            return
        }

        var node = clickedElem.parentNode
        if ($(node).hasClass('jbfm-leaf')) {
            return
        }

        if (node.getElementsByTagName('LI').length) {

            switchNodeClass(node)

            $.ajax({
            url: url,
            data: {op:"unlist",path:encodeURIComponent(buildNodePath(node))},
            dataType: "json",          
            cache: false
            })

            return
		}
        listNode(node)
    })

    //list root node after initialization:
    listNode(rootLI)
}
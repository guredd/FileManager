/**
 * Created by IntelliJ IDEA.
 * User: guredd
 * Date: 10.10.2010
 * Time: 17:01:33
 * To change this template use File | Settings | File Templates.
 */
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

    function switchNodeClass(node) {
        if($(node).hasClass('jbfm-opened')) {
            $(node).removeClass('jbfm-opened')
            $(node).addClass('jbfm-closed')
        } else {
            $(node).removeClass('jbfm-closed')
            $(node).addClass('jbfm-opened')
        }
    }

    function listNode(node) {

        var onSuccess = function(data) {
            if (!data.errcode) {
                onLoaded(data.nodes)
                showProgress(false)
            } else {
                showProgress(false)
                onLoadError(data)
            }
        }

        var onAjaxError = function(xhr, status) {
            showProgress(false)
            var errinfo = { errcode: status }
            if (xhr.status != 200) {
                // ����� ���� ������ 200, � ������
                // ��-�� ������������� JSON
                errinfo.message = xhr.statusText
            } else {
                errinfo.message = '������������ ������ � �������'
            }
            onLoadError(errinfo)
        }

        var onLoadError = function(error) {
            var msg = "Error "+error.errcode
            if (error.message) msg = msg + ' :'+error.message
            alert(msg)
        }


        function showProgress(on) {
            var expand = node.getElementsByTagName('DIV')[0]
            expand.className = on ? 'jbfm-progress' : 'jbfm-expand'
        }

        function onLoaded(nodes) {

            node.getElementsByTagName('UL')[0].innerHTML = ''

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
            }

            node.isLoaded = true
            switchNodeClass(node)
        }

        // get node type from associated jbfm-type-... class
        function getNodeType() {
            var classes = node.getElementsByTagName('DIV')[1].className
            var arr = classes.split(' ',5)
            var class = ''
            for(var i=0; i<arr.length; i++) {
                if(arr[i].substr(0,10) == 'jbfm-type-' && arr[i] != 'jbfm-type-default') {
                    class = arr[i]
                    break
                }
            }
            return class.substr(10,class.length-10)
        }

        // build node path as string for ajax request
        function buildNodePath() {
            var inode = node
            var path = ''
            while(!$(inode).hasClass('jbfm-root')) {
                path = '/' + inode.getElementsByTagName('DIV')[2].innerHTML + path
                inode = inode.parentNode.parentNode
            }
            return path
        }

        showProgress(true)

        $.ajax({
            url: url,
            data: {op:"list",type:getNodeType(),path:buildNodePath()},
            dataType: "json",
            success: onSuccess,
            error: onAjaxError,
            cache: false
        })
    }

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
            
        listNode(node)
    })
}
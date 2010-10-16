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
                onLoaded(data)
                showProgress(false)
            } else {
                showProgress(false)
                onLoadError(data)
            }
        }

        var onAjaxError = function(xhr, status) {
            showLoading(false)
            var errinfo = { errcode: status }
            if (xhr.status != 200) {
                // может быть статус 200, а ошибка
                // из-за некорректного JSON
                errinfo.message = xhr.statusText
            } else {
                errinfo.message = 'Некорректные данные с сервера'
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

        function onLoaded(data) {

            for(var i=0; i<data.length; i++) {
                var child = data[i]
                var li = document.createElement('LI')
                li.id = child.id

                $(li).addClass('jbfm-node')

                if(child.isFolder) {
                     $(li).addClass('jbfm-closed')
                } else {
                     $(li).addClass('jbfm-leaf')
                }

                if (i == data.length-1) $(li).addClass('jbfm-last')

                li.innerHTML = '<div class="jbfm-expand"></div><div class="jbfm-name">'+child.title+'</div>'
                if (child.isFolder) {
                    li.innerHTML += '<ul class="jbfm-container"></ul>'
                }
                node.getElementsByTagName('UL')[0].appendChild(li)
            }

            node.isLoaded = true
            switchNodeClass(node)
        }


        showProgress(true)


        $.ajax({
            url: url,
            data: node.id,
            dataType: "json",
            success: onSuccess,
            error: onAjaxError,
            cache: false
        })
    }

    $(rootUL).click = function(event) {
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
    }
}
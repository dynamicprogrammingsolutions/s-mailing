
function addEventListenerOnSelector(element,event,selector,callback)
{
    element.addEventListener(event,function(event) {
        element.querySelectorAll(selector).forEach(function(t) {
            var current = event.target;
            while(true) {
                if (current == t) {
                    callback.call(this,event,current);
                }
                if (current.parentNode) {
                    current = current.parentNode;
                } else {
                    break;
                }
            }
        }.bind(this))
    }.bind(this))
}

HTMLElement.prototype.addEventListenerOnSelector = function(event,selector,callback) {
    addEventListenerOnSelector(this,event,selector,callback);
}

window.addEventListener("load",function(event) {
    console.log("loaded: ",event);

    addEventListenerOnSelector(document.body,"click","a.button",function(event,target) {
        event.preventDefault();
    })

    addEventListenerOnSelector(document.body,"click","button:not([type=submit])",function(event,target) {
        event.preventDefault();
    })

});

$(document).ready(function() {
    if (window.location.pathname.endsWith("login")) {
        if (window.location.hash) {
            var form = $(".login form")[0];
            var action = form.getAttribute("action");
            $.ajax({
                url: action+"/hash",
                method: "POST",
                data: window.location.hash,
                contentType: 'text/plain'
            })
        }
    }
})
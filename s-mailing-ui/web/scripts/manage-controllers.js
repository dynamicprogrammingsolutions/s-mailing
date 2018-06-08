
var pagecontrollers = {};

$(document).ready(function() {
    console.log("load event");
    var elements = $("div").each(function(i,element) {
        if (element.dataset !== undefined && element.dataset.controller !== undefined) {
            if (pagecontrollers[element.dataset.controller] !== undefined) {
                console.log("init controller ", element.dataset.controller);
                pagecontrollers[element.dataset.controller].init(element);
            } else {
                console.log("controller "+element.dataset.controller+" not found");
            }
        }
    });
});

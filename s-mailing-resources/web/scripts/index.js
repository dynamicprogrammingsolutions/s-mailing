
$(document).ready(function() {

    function loadPage(page) {
        $.ajax({
            url: "views/"+page+".html",
            dataType: "html",
            success: function(data) {
                var section = $("#mainSection");
                section.empty().append(data);
                loadModule("controllers/"+page,function(name,module) {
                    module.create(section);
                });
            },
            error: function() {
                $.ajax({url:"views/404.html",success:function(data){
                    $("#mainSection").empty().append(data);
                }})
            }
        })
    }

    $("#sideBar nav a").click(function(event) {
        event.preventDefault();
        var element = event.currentTarget;
        window.location.hash = element.dataset.page;
        loadPage(element.dataset.page);
    });

    if (window.location.hash != null && window.location.hash.length !== 0) {
        page = window.location.hash;
        page = page.substring(1);
        loadPage(page);
    }

});

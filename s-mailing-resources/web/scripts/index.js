
$(document).ready(function() {

    $("#mainSection").on("click","#itemList #btnAddTask",function() {
        $("#createItem").removeClass("hidden");
    });

    function loadPage(page) {
        $("#mainSection").load("views/"+page+".html",function(response,status) {
            if (status == "error") {
                $("#mainSection").load("views/404.html");
            } else {
                $.getScript("scripts/controllers/"+page+".js");
            }
        });
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

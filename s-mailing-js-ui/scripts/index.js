
$(document).ready(function() {
    $("#taskRow").tmpl({name:"name1",subject:"subject1",id:"1"}).appendTo($("#itemsTable > tbody"));
    $("#taskRow").tmpl({name:"name2",subject:"subject2",id:"2"}).appendTo($("#itemsTable > tbody"));
    console.log("add click");
    $("#itemList #btnAddTask").on("click",function() {
        $("#createItem").removeClass("hidden");
    });
    x = new Func1();
});
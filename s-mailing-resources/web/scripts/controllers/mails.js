(function() {
    var mailManager = new MailManager();


    mailManager.getFirstAndMax(console.log,console.log);

    mailManager.getAll(function(result){
        console.log(result);
        $("#taskRow").tmpl(result).appendTo($("#itemsTable > tbody"));

    }, function() {

    })


})();

    $(document).ready(function() {

        errorLogger = function(code) {
            console.log("error: ",code);
        };

        setTimeout(function() {
            console.log("2nd test");
            mail = {name:"mail",subject:"subject",from:"from@test.com",body_text:"Body"};
            mailManager.create(mail,function(createdMail) {
                console.log("mail created: ", createdMail);
                mailManager.find(createdMail.id,function(mail) {
                    console.log("mail got: ",mail);
                    mail.subject = "subject_modified";
                    mailManager.edit(mail.id,mail,function() {
                        console.log("mail modified: ",mail);
                        /*mailManager.addMailToCampaign(mail.id,1,function() {
                            console.log("added to campaign");*/
                            mailManager.remove(createdMail.id,function () {
                                console.log("mail deleted");
                            },errorLogger);
                        //},errorLogger)
                    },errorLogger);
                },errorLogger);
            },function() { console.log("error"); });
        },1000);

        setTimeout(function(){
            mailManager.getRange(5,5,function(data) {
                console.log("5 mails starting at 5: ",data);
            },errorLogger)
        },2000);

        setTimeout(function(){
            mailManager.getAll(function(data) {
                console.log("all mails: ",data);
            },errorLogger);
        },3000);

        setTimeout(function(){
            mailManager.getAll(function(data) {
                console.log("all mails: ",data);
                for (i = 0; i < data.length; i++) {
                    mailManager.getMailCampaigns(data[i].id,function(data) {
                        console.log("mail campaigns: ",data);
                    },errorLogger)
                }

            },errorLogger);
        },4000);

        setTimeout(function() {
            userManager.getAll(function(data){
                console.log("all users: ", data);
            },errorLogger);
        },5000);

    });

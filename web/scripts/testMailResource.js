
    testAsync("Test Test",function(ok,fail) {
        MailResource.test({$callback:function(status,res,data){
            ok("msg");
        }});
    })



    /*MailResource.getMail({$callback:function(status,res,data){ console.log(data); }});

        MailResource.newMail({$entity:{name:"mail",subject:"subject",from:"from@test.com",body_text:"Body"},
            $callback:function(status,res,data){
                console.log("send delete: "+data);
                MailResource.deleteMail({id:data.id,$callback:function(s,r,data){console.log(s,data);}});
                console.log(data);
            }
        });*/
        
    
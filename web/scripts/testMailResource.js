/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    function testAsync(name,testCallback)
    {
        QUnit.test(name, function(assert) {
            var done = assert.async(50);
            var okCallback = function (message) {
                assert.ok(true,message?message:"OK");
                console.log("ok");
                done();
            };
            var failCallback = function (message) {
                assert.ok(false,message?message:"Fail");
                done();
            };
            var timeoutCallback = function () {
                assert.ok(false,"Timeout Reached");
                done();
            };
            testCallback(okCallback,failCallback);
            //setTimeout(timeoutCallback,2000);
        });
    }
    
    testAsync("Test Test",function(ok,fail) {
        MailResource.test({$callback:function(status,res,data){
            console.log(data);
            console.log("OK");
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
        
    
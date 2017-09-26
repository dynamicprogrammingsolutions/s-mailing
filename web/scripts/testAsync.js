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

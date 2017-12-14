module("libs/jsUtilities",[],function() {
    return {
        extend: function(obj,propsObj)
        {
            for (p in propsObj) {
                obj[p] = propsObj[p];
            }
        }
    }
});


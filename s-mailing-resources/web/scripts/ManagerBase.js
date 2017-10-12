function ManagerBase() {

}

ManagerBase.prototype.getAll = function(success,error) {
    this.resource.getAll({$callback:function(status,request,data) {
        if (status == 200) {
            success(data);
        } else {
            error(status,request);
        }
    }})
}

ManagerBase.prototype.getRange = function(first,max,success,error) {
    this.resource.getRange({first:first,max:max,$callback:function(status,request,data) {
        if (status == 200) {
            success(data);
        } else {
            error(status,request);
        }
    }})
}


ManagerBase.prototype.create = function(mail, success, error) {
    this.resource.create({$entity:mail,$callback:function (status,request,data) {
        if (status == 200) {
            success(data);
        } else {
            error(status,request);
        }

    }});
}

ManagerBase.prototype.find = function(id,success,error) {
    this.resource.find({id:id,$callback:function(status,request,data) {
        if (status == 200) {
            success(data);
        } else {
            error(status,request);
        }
    }})
}

ManagerBase.prototype.edit = function(id,mail,success,error) {
    this.resource.edit({id:id,$entity:mail,$callback:function(status,request,data) {
        if(status == 204) {
            success();
        } else {
            error(status,request);
        }
    }});
}

ManagerBase.prototype.remove = function(id,success,error) {
    this.resource.remove({id:id,$callback:function(status,request,data){
        if (status == 204) {
            success();
        } else {
            error(status,request);
        }
    }});
}



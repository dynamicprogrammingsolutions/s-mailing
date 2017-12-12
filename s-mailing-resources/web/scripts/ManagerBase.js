function ManagerBase() {

}

ManagerBase.prototype.ajax = function(url,method,data,expectData,success,error) {
    var ajaxObj = {
        url: url,
        method: method,
    }
    if (data !== null) {
        ajaxObj.contentType = "application/json";
        ajaxObj.processData = false;
        ajaxObj.data = JSON.stringify(data);
    }
    var expectedStatus;
    if (expectData) {
        ajaxObj.dataType = "json";
        expectedStatus = 200;
    } else {
        expectedStatus = 204;
    }
    ajaxObj.success = function(data,status,response) {
        if (response.status == expectedStatus) {
            success(data);
        } else {
            error(response.status,response);
        }
    }
    ajaxObj.error = function(response) {
        error(response.status,response);
    }
    $.ajax(ajaxObj);
}

ManagerBase.prototype.getAll = function(success,error) {

    this.ajax(this.urlBase,"GET",null,true,success,error);

}

ManagerBase.prototype.getFirstAndMax = function (success,error) {
    this.ajax(this.urlBase+"/?first=0&max=-1","GET",null,true,function(data) {
        console.log(data);
    },error);
}

ManagerBase.prototype.getRange = function(first,max,success,error) {

    this.ajax(this.urlBase+"/?first="+first+"&max="+max,"GET",null,true,success,error);

}


ManagerBase.prototype.create = function(mail, success, error) {

    this.ajax(this.urlBase,"POST",mail,true,success,error);

}

ManagerBase.prototype.find = function(id,success,error) {

    this.ajax(this.urlBase+"/"+id,"GET",null,true,success,error);

}

ManagerBase.prototype.edit = function(id,mail,success,error) {

    this.ajax(this.urlBase+"/"+id,"PUT",mail,false,success,error);

}

ManagerBase.prototype.remove = function(id,success,error) {

    this.ajax(this.urlBase+"/"+id,"DELETE",null,false,success,error);

}



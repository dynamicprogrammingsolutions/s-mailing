function MailManager() {
    this.resource = MailResource;
    this.urlBase = "http://localhost:8080/s-mailing/resources/service/mails";
}

MailManager.prototype = new ManagerBase();
MailManager.prototype.constructor = MailManager;

MailManager.prototype.getMailCampaigns = function(id,success,error) {

    this.ajax(this.urlBase+"/"+id+"/campaigns","GET",null,true,success,function(status,response) {
        if (status == 204) success([]);
        else error(status,response);
    });

}

mailManager = new MailManager();
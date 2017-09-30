function MailManager() {
    this.resource = MailResource;
}

MailManager.prototype = new ManagerBase();
MailManager.prototype.constructor = MailManager;

MailManager.prototype.getMailCampaigns = function(id,success,error) {
    this.resource.getMailCampaigns({id:id,$callback:function(status,request,data) {
        if (status == 200) {
            success(data);
        } else if (status == 204) {
            success([]);
        } else {
            error(status,request);
        }
    }})
}

MailManager.prototype.addMailToCampaign = function(id,campaignId,success,error) {
    this.resource.addMailToCampaign({id:id,campaignId:campaignId,$callback:function(status,request,data) {
        if (status == 204) {
            success();
        } else {
            error(status,request);
        }
    }})
}

mailManager = new MailManager();
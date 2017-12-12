UserManager = function () {
    this.urlBase = "http://localhost:8080/s-mailing/resources/service/users";
};

UserManager.prototype = new ManagerBase();
UserManager.prototype.constructor = UserManager;

userManager = new UserManager();

UserManager = function () {
    this.resource = UserResource;
};

UserManager.prototype = new ManagerBase();
UserManager.prototype.constructor = UserManager;

userManager = new UserManager();

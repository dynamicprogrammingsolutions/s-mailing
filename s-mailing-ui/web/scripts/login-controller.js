function LoginController() {
    this.element = null;
    this.initialized = false;
    this.resource = null;
    this.modal = null;
    this.callback = null;
}

LoginController.prototype.show = function() {
    this.modal.classList.add("shown");
}

LoginController.prototype.hide = function() {
    this.modal.classList.remove("shown");
}

LoginController.prototype.login = function(callback) {
    if (callback) this.callback = callback;
    this.show();
}

LoginController.prototype.init = function(element) {

    if (this.initialized) return;
    this.initialized = true;
    this.element = element;
    this.modal = element.parentNode;
    this.url = element.dataset.url;

    console.log("login controller initialized");

    this.element.querySelector("button").addEventListener("click",function(event) {

        var form = this.element.querySelector(".login form");
        var credentials = {
            username: form.username.value,
            password: form.password.value
        }
        form.username.value = "";
        form.password.value = "";
        console.log(credentials);

        $.ajax({
            url: this.url,
            method: "POST",
            contentType: "application/json",
            processData: false,
            data: JSON.stringify(credentials),
            success: function() {
                this.hide();
                if (this.callback) {
                    this.callback();
                }
            }.bind(this),
            fail: function(status, data) {
                console.log(status, data);
            }.bind(this)
        })
    }.bind(this));

}

pagecontrollers['login-controller'] = (function() {

    return new LoginController();

}());

function CrudController() {
    this.element = null;
    this.initialized = false;
    this.resource = null;
    this.messagesResource = null;
}

CrudController.prototype.ajax = function(url,method,data,callback,onerror) {
    var runRequest = function() {
        $.ajax({
            url: url,
            method: method,
            contentType: "application/json",
            processData: false,
            data: data?JSON.stringify(data):null,
            success: function(data) {
                callback.bind(this)(data);
            }.bind(this),
            error: function(xhr) {
                if (xhr.status == 401) {
                    pagecontrollers["login-controller"].login(runRequest);
                } else {
                    if (onerror) {
                        onerror(xhr);
                    }
                }
            }.bind(this)
        })
    }.bind(this);
    runRequest();
}

CrudController.prototype.controls = null;

CrudController.prototype.setControls = function() {
    this.controls = {
        editSection: this.element.children["edit-item"],
        editForm: this.element.children["edit-item"].children["item-form"],
        createSection: this.element.children["create-item"],
        createForm: this.element.children["create-item"].children["item-form"],
        showSection: this.element.children["show-item"],
        table: this.element.children['itemtable'],
    }
}


CrudController.prototype.init = function(element) {

    if (this.initialized) return;
    this.initialized = true;
    this.element = element;
    this.resource = element.dataset.resource;
    this.messagesResource = element.dataset.messages;

    this.setControls();



    $(this.element).find(".datepicker").datepicker({
        dateFormat: "yy-mm-dd"
    })

    // *** New Item ***
    $(this.element).find(".new-item").click(this.onNewItem.bind(this))

    // *** Submit new item ***
    $(this.element).find(".section.create-item .submit-item").click(this.onSubmitNewItem.bind(this))

    // *** Submit modify item ***
    $(this.element).find(".section.edit-item .submit-item").click(this.onSubmitModifyItem.bind(this))

    // *** Show Item ***
    $(this.element).find(".itemtable").on("click",".show-item",this.onShowItem.bind(this))

    // *** Modify Item ***
    $(this.element).find(".itemtable").on("click",".modify-item",this.onModifyItem.bind(this))

    // *** Delete Item ***
    $(this.element).find(".itemtable").on("click",".delete-item",this.onDeleteItem.bind(this))

    if (window.location.hash) {
        var id = window.location.hash.substring(1);
        if (!isNaN(id)) {
            this.modifyItem(id);
        }
    }

}

CrudController.prototype.onNewItem = function(event) {
    $(this.element).find(".edit-item").addClass("hidden");
    $(this.element).find(".create-item").removeClass("hidden");
}
/**
 *
 * @param {XMLHttpRequest} xhr
 */
CrudController.prototype.getMessages = function(xhr) {
    var messages = this.element.children["messages"];
    if (messages) {
        var errorTemplate = this.element.children["tpl-error-message"];
        var successTemplate = this.element.children["tpl-success-message"];
        $(messages).empty();
        var showMessage = function (t) {
            var template;
            if (t.type === "Error") template = errorTemplate;
            if (t.type === "Success") template = successTemplate;
            var element = TemplateEngine.createElement(template, t);
            messages.appendChild(element);
            messages.scrollIntoView();
            console.log("message: ", t);

        }
        if (xhr) {
            if (xhr.status === 400 && xhr.responseText !== "") {
                showMessage({type: "Error", message: xhr.responseText});
            } else {
                showMessage({type: "Error", message: xhr.statusText});
            }
        }
        if (this.messagesResource) {
            this.ajax(this.messagesResource, "GET", null, function (data) {
                data.forEach(showMessage)
            });
        }
    } else {
        console.log("no message container");
    }
}

CrudController.prototype.onSubmitNewItem = function(event) {
    var form = $(this.element).find(".create-item .item-form")[0];
    var item = this.convertFormToItem(form);
    this.ajax(this.resource,"POST",item,function(data) {
        this.onNewItemAdded(data,function() {
            this.emptyForm(form);
            $(this.element).find(".section.create-item").addClass("hidden");
            this.refreshTable();
            this.getMessages();
        }.bind(this));
    },function(xhr) {
        this.getMessages(xhr);
    }.bind(this))
}

CrudController.prototype.onNewItemAdded = function(data,defaultSuccessAction) {
    defaultSuccessAction();
}

CrudController.prototype.onModifyItem = function(event) {
    var target = event.currentTarget;
    var id = target.dataset.id;
    this.modifyItem(id);
}

CrudController.prototype.modifyItem = function(id) {
    var form = $(this.element).find(".edit-item .item-form")[0];
    $(this.element).find(".create-item").addClass("hidden");
    this.ajax(this.resource+"/"+id,"GET",null,function(data) {
        this.convertItemToForm.bind(this)(form,data);
        $(this.element).find(".section.edit-item").removeClass("hidden");
        this.getMessages();
    },function(xhr) {
        this.getMessages(xhr);
    }.bind(this))
}

CrudController.prototype.onSubmitModifyItem = function(event) {
    var form = $(this.element).find(".section.edit-item .item-form")[0];
    var item = this.convertFormToItem(form);
    this.ajax(this.resource+"/"+item.id,"PUT",item,function(data) {
        this.onItemModified(item.id,item,function() {
            this.emptyForm(form);
            $(this.element).find(".section.edit-item").addClass("hidden");
            this.refreshTable();
            if (this.shownData && item.id == this.shownData.id) {
                console.log("update shown item")
                this.showItem(item.id);
            }
            this.getMessages();
        }.bind(this))
    },function(xhr) {
        this.getMessages(xhr);
    }.bind(this));
}


CrudController.prototype.onItemModified = function(id,data,defultSuccessAction) {
    defultSuccessAction();
}

CrudController.prototype.onShowItem = function(event) {
    var target = event.currentTarget;
    var id = target.dataset.id;
    this.showItem(id);
}

CrudController.prototype.showItem = function(id) {
    var showItem = $(this.element).find(".show-section")[0];
    this.ajax(this.resource+"/"+id,"GET",null,function(data) {
        this.shownData = data;
        showItem.innerHTML = "";
        showItem.innerHTML = this.convertItemToShow.bind(this)(data);
    })
}

CrudController.prototype.onDeleteItem = function(event) {
    var target = event.currentTarget;
    var id = target.dataset.id;
    this.ajax(this.resource+"/"+id,"DELETE",null,function() {
        this.refreshTable(500)
        this.getMessages()
    }.bind(this),function(xhr) {
        this.getMessages(xhr)
    }.bind(this));
}

CrudController.prototype.refreshTable = function(delay) {
    $(this.element).find(".itemtable")[0].controller.refresh(delay);
}

CrudController.prototype.convertItemToShow = function(data) {
    var template = this.element.children["tpl-show-item"];
    return TemplateEngine.applyTemplate(template,data);
}
CrudController.prototype.convertFormToItem = function(form) {

    var result = {};
    $.each($(form).find(':input'),function(i,v) {
        var name = $(v).attr('name');
        var value = $(v).val();
        if (name) {
            if ($(v).attr("type") === "radio") {
                if ($(v).is(":checked")) {
                    result[name] = value;
                }
            }
            if ($(v).attr("type") === "checkbox") {
                if (value !== "on" && !result[name]) {
                    result[name] = [];
                }
                if ($(v).is(":checked")) {
                    if (value === "on") {
                        result[name] = true;
                    } else {
                        result[name].push({id: value});
                    }
                } else {
                    if (value === "on") {
                        result[name] = false;
                    }
                }
            }
            if ($(v).is("textarea") || ($(v).is("input") && (["text","password","hidden"].includes($(v).attr("type"))))) {
                result[name] = value;
            }
        }
    })
    console.log(result);
    return result;


}

CrudController.prototype.convertItemToForm = function(form, data) {

    this.emptyForm(form);


    $.each($(form).find(':input'),function(i,v) {
        var name = $(v).attr('name');
        if (data[name]) {
            if ($(v).attr("type") === "radio") {
                if (data[name] == $(v).val()) {
                    $(v).prop("checked",true);
                }
            } else if ($(v).attr("type") !== "checkbox") {
                $(v).val(data[name]);
            } else {
                if (Array.isArray(data[name])) {
                    data[name].forEach(function(t) {
                        if (t.id && t.id == $(v).val()) {
                            $(v).prop("checked",true);
                        }
                    })
                } else {
                    if (data[name] === true) {
                        $(v).prop("checked",true);
                    }
                }
            }
        } else {
            if ($(v).attr("type") != "checkbox" && $(v).attr("type") != "radio") {
                $(v).val('');
            }
        }
    });




}


CrudController.prototype.emptyForm = function(form) {


    $.each($(form).find(':input'),function(i,v) {
        if ($(v).attr("type") == "file") {
            var oldInput = v;
            var newInput = document.createElement("input");
            newInput.type = "file";
            newInput.id = oldInput.id;
            newInput.name = oldInput.name;
            newInput.className = oldInput.className;
            newInput.style.cssText = oldInput.style.cssText;
            oldInput.parentNode.replaceChild(newInput, oldInput);
            return;
        }
        if ($(v).attr("type") == "checkbox" || $(v).attr("type") == "radio") {
            $(v).prop("checked",false);
            return;
        }
        $(v).val('');
    });

}

pagecontrollers['crud-controller'] = (function() {

    return new CrudController();

}());

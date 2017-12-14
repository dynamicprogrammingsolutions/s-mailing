module("controllers/mails", ["libs/jsUtilities","MailManager"], function(require) {

var Utils = require("libs/jsUtilities");
var mailManager = require("MailManager");

function MailsController(element) {
    this.init($(element));
    this.refresh();
}

Utils.extend(MailsController.prototype, {
    itemsPerPage: 10,
    currentPage: 1,
    element: null,
    loadedData: null,

    init: function(element) {
        this.element = element;
        var self = this;
        element.on("click","#itemList #btnAddTask",function(event) {
            event.preventDefault();
            self.createItem();
        });
        element.find("#tblNav").on("click","a",function(event) {
            event.preventDefault();
            self.pageClick.call(self,event);
        });
        element.find(".saveItem").on("click",function(event) {
            event.preventDefault();
            self.saveItem();
        });
        element.find("tbody").on("mouseenter","tr .preview-on-hover",function(event) {

            self.previewHandler.call(self,event);

        });
    },

    previewHandler: function(event) {

        var self = this;

        var elem = event.currentTarget;
        var e = event.originalEvent;

        if ($(elem).find(".preview").length) return;

        var shown = false;
        var disabled = false;
        var stick = false;

        setTimeout(function() {
            if (!disabled) {
                var id = elem.dataset.id;
                var data;
                if (self.loadedData !== undefined) {
                    for (var i = 0; i < self.loadedData.length; i++) {
                        if (self.loadedData[i].id === +id) {
                            data = self.loadedData[i];
                        }
                    }
                }
                if (data !== undefined) {
                    //console.log(data);
                    var previewElement = $("#rowPreview").tmpl(data)[0];
                    previewElement = elem.appendChild(previewElement);
                    shown = true;
                }
            }
        },1000);

        var clickhandler;
        var handler;
        //console.log("add click listener");
        elem.addEventListener("click",clickhandler = function() {
            stick = !stick;
            //console.log("click: ",stick);
        });

        //console.log("add mouseleave listener");
        elem.addEventListener("mouseleave",handler = function() {
            //console.log("mouseleave: ",stick);
            if (!stick) {
                //console.log("remove mouseleave listener");
                elem.removeEventListener("mouseleave", handler);
                //console.log("remove click listener");
                elem.removeEventListener("click", clickhandler);
                if (shown) {
                    $(elem).find(".preview").remove();
                }
                disabled = true;
            }
        });
    },

    saveItem: function() {
        var self = this;
        var form = $(this.element).find("#createItemForm");
        if (form.valid()) {
            var item = form.toObject();
            console.log(item);
            mailManager.create(item,function() {
                //$('#taskCreation',taskPage).addClass('not');
                self.refresh();
                //$('#taskCreation :input',taskPage).val('');
            },self.errorLogger);
        }
    },

    pageClick: function(event) {
        if ($(event.currentTarget).hasClass("tablePage")) {
            this.currentPage = event.currentTarget.dataset.page;
            this.refresh();
        } else if ($(event.currentTarget).hasClass("tablePrevPage")) {
            console.log("prev");
            this.currentPage--;
            this.refresh();
        } else if ($(event.currentTarget).hasClass("tableNextPage")) {
            this.currentPage++;
            this.refresh();
        }
    },

    createItem: function() {
        $("#createItem").removeClass("hidden");
    },

    errorLogger: function(result) {
        console.log("Error: ",result);
    },

    refreshPagination: function(maxPages, currentPage) {

        var currentNode = $('#tblNav > .tablePrevPage');
        $('#tblNav .tablePage').remove();

        for (var i = 1; i <= maxPages; i++) {
            var nextNode = $('#tblPage').tmpl({page:i});
            currentNode.after(nextNode);
            currentNode = nextNode;
            if (i == currentPage) {
                currentNode.addClass("current");
            }
        }

        if (currentPage == 1) {
            $('#tblNav > .tablePrevPage').addClass('disabled');
        } else {
            $('#tblNav > .tablePrevPage').removeClass('disabled');
        }

        if (currentPage == maxPages) {
            $('#tblNav > .tableNextPage').addClass('disabled');
        } else {
            $('#tblNav > .tableNextPage').removeClass('disabled');
        }

        $("#itemList").removeClass("loading");

    },

    refresh: function() {

        var self = this;

        var itemsPerPage = this.itemsPerPage;
        var currentPage = this.currentPage;

        $("#itemsTable").addClass("loading");

        mailManager.getCount(function(count) {
            console.log("itemsPerPage: ",itemsPerPage);
            var maxPages = Math.floor(((count-1)/itemsPerPage)+1);
            console.log("count: ",count," maxPages: ",maxPages);
            if (currentPage < 1) currentPage = 1;
            if (currentPage > maxPages) currentPage = maxPages;
            var start = (currentPage-1)*itemsPerPage;
            if (start >= count || start < 0) {
                currentPage = 1;
                start = 0;
            }
            self.currentPage = currentPage;
            console.log("page: ",currentPage);
            console.log("start: ",start," count: ",count);

            mailManager.getRange(start,itemsPerPage,function(result){

                console.log(result);
                $("#itemsTable > tbody").empty();
                self.loadedData = result;
                $("#taskRow").tmpl(result).appendTo($("#itemsTable > tbody"));

                $("#itemsTable").removeClass("loading");

                self.refreshPagination(maxPages,currentPage);

            }, this.errorLogger);

        },this.errorLogger);

    }

});

return {
    create: function(element) {
        new MailsController(element);
    }
};

});
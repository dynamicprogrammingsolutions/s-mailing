pagecontrollers['table-controller'] = (function() {

    var controller = {
        element: null,
        initialized: false,
        resource: null,
        itemsPerPage: 10,
        currentPage: 1,
        init: function(element) {

            if (controller.initialized) return;
            controller.initialized = true;
            controller.element = element;
            controller.element.controller = this;

            console.log("table-controller initialized");
            console.log("resource: "+element.dataset.resource);

            $(element).find(".tblNav").on("click","a",function(event) {
                event.preventDefault();
                this.pageClick.bind(this)(event);
            }.bind(this));

            this.resource = element.dataset.resource;

            this.refresh();

        },

        refresh: function(delay) {
            var table = this.element.children["table"];
            var template = this.element.children["tpl-row"];
            var body = table.querySelector("tbody");
            var itemsPerPage = this.itemsPerPage;
            var currentPage = this.currentPage;
            this.element.children["table"].classList.add("loading");

            var refresh = function() {
                $.ajax({
                    url: this.resource+"/count",
                    success: function(data) {
                        var count = data;
                        //console.log("itemsPerPage: ",itemsPerPage);
                        var maxPages = Math.floor(((count-1)/itemsPerPage)+1);
                        //console.log("count: ",count," maxPages: ",maxPages);
                        if (currentPage < 1) currentPage = 1;
                        if (currentPage > maxPages) currentPage = maxPages;
                        var start = (currentPage-1)*itemsPerPage;
                        if (start >= count || start < 0) {
                            currentPage = 1;
                            start = 0;
                        }
                        this.currentPage = currentPage;
                        //console.log("page: ",currentPage);
                        //console.log("start: ",start," count: ",count);

                        $.ajax({
                            url:  this.resource+"?first="+start+"&max="+itemsPerPage,
                            success: function(data) {
                                //console.log(data);
                                while(body.firstChild) {
                                    body.removeChild(body.firstChild);
                                }
                                data.forEach(function (t, number, ts) {
                                    var element = document.createElement("tr");
                                    element.innerHTML = TemplateEngine.applyTemplate(template,t,this.convertData);
                                    body.appendChild(element);
                                }.bind(this));

                                this.refreshPagination(maxPages,currentPage);

                                //controller.addEvents(body);
                            }.bind(this),
                            error: function(err) {

                                if (pagecontrollers['crud-controller']) {
                                    pagecontrollers['crud-controller'].getMessages.bind(pagecontrollers['crud-controller'])(err);
                                }

                                this.element.children["table"].classList.remove("loading");

                                console.log(err);
                            }.bind(this)
                        });

                    }.bind(this),
                    error: function(err) {
                        console.log(err);
                        if (pagecontrollers['crud-controller']) {
                            pagecontrollers['crud-controller'].getMessages.bind(pagecontrollers['crud-controller'])(err);
                        }
                        this.element.children["table"].classList.remove("loading");
                    }.bind(this)
                })
            }.bind(this)

            if (delay) {
                window.setTimeout(function() {
                    refresh();
                }.bind(this),delay)
            } else {
                refresh();
            }

        },

        refreshPagination: function(maxPages, currentPage) {

            var pagination = this.element.children["pagination"];
            var nav = pagination.children["tblNav"];
            var currentNode = nav.children["tablePrevPage"];
            $(nav).find('.tablePage').remove();
            var tmp = pagination.children["tblPage"];

            for (var i = 1; i <= maxPages; i++) {
                var element = TemplateEngine.createElement(tmp,{page:i});
                $(currentNode).after(element);
                currentNode = element;
                if (i == currentPage) {
                    currentNode.classList.add("current");
                }
            }

            if (currentPage == 1) {
                nav.children["tablePrevPage"].classList.add('disabled');
            } else {
                nav.children["tablePrevPage"].classList.remove('disabled');
            }

            if (currentPage == maxPages) {
                nav.children["tableNextPage"].classList.add('disabled');
            } else {
                nav.children["tableNextPage"].classList.remove('disabled');
            }

            this.element.children["table"].classList.remove("loading");

        },

        pageClick: function(event) {
            if ($(event.currentTarget).hasClass("tablePage")) {
                this.currentPage = event.currentTarget.dataset.page;
                this.refresh();
            } else if ($(event.currentTarget).hasClass("tablePrevPage")) {
                this.currentPage--;
                this.refresh();
            } else if ($(event.currentTarget).hasClass("tableNextPage")) {
                this.currentPage++;
                this.refresh();
            }
        },

        convertData: function(d) {
            if (d === true) return '<input class="intable" type="checkbox" disabled checked>';
            if (d === false) return '<input class="intable" type="checkbox" disabled>';
            return d;
        }


    };

    return controller;

}());

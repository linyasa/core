System.register(["angular2/core", "angular2/router", 'angular2/http'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var core_1, router_1, http_1;
    var HeaderComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (http_1_1) {
                http_1 = http_1_1;
            }],
        execute: function() {
            HeaderComponent = (function () {
                function HeaderComponent(http, router) {
                    this.http = http;
                    this.router = router;
                }
                HeaderComponent.prototype.ngOnInit = function () {
                    /*this.menus = [{
                     tabName: 'T1',
                     tabDescription: 'AAAAA',
                     url: 'http://localhost:8080/aaaaa',
                     menuItems: [
                     {
                     name: 'T11',
                     url: 'http://localhost:8080/bbbbb'
                     },
                     {
                     name: 'T12',
                     url: 'http://localhost:8080/bbbbb'
                     },
                     {
                     name: 'ANGULAR_PORTLET3',
                     url: 'http://localhost:8080/bbbbb'
                     }
                     ]
                     },
                     {
                     tabName: 'T2',
                     tabDescription: 'BBB',
                     url: 'http://localhost:8080/bbbb',
                     menuItems: [
                     {
                     name: 'T11',
                     url: '/bbbbb'
                     }
                     ]
                     }];*/
                    var _this = this;
                    this.http.get('/api/core_web/menu')
                        .subscribe(function (res) { return _this.menus = res.json(); });
                };
                HeaderComponent = __decorate([
                    core_1.Component({
                        selector: "header-component",
                        styles: ["\n        .t1 {\n          font-size: 12px;\n        }\n        .t2{\n        font-size: 10px;\n        }\n    "],
                        template: "\n    <div class=\"container\">\n        <span *ngFor=\"#menu of menus\" style=\"display: inline; float: left;\">\n            <a href=\"{{menu.url}}\"><label class=\"t1\">{{menu.tabName}}</label></a>\n            <ul>\n                <li *ngFor=\"#menuItem of menu.menuItems\">\n                <a href=\"{{menuItem.url}}\" *ngIf=\"!menuItem.angular\">\n                    <label class=\"t2\">{{menuItem.name}}</label>\n                </a>\n                <a [routerLink]=\"[menuItem.id]\" *ngIf=\"menuItem.angular\">\n                    <label class=\"t2\">{{menuItem.name}}</label>\n                </a>\n                </li>\n            </ul>\n        </span>\n    </div>\n    ",
                        providers: [[http_1.HTTP_PROVIDERS]],
                        directives: [router_1.RouterLink]
                    }), 
                    __metadata('design:paramtypes', [http_1.Http, router_1.Router])
                ], HeaderComponent);
                return HeaderComponent;
            }());
            exports_1("HeaderComponent", HeaderComponent);
        }
    }
});
//# sourceMappingURL=HeaderComponent.js.map
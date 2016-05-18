System.register(["angular2/core"], function(exports_1, context_1) {
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
    var core_1;
    var SearchComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            SearchComponent = (function () {
                function SearchComponent() {
                    this.queryChange = new core_1.EventEmitter();
                    this.query = null;
                }
                SearchComponent.prototype.search = function ($event) {
                    // TODO: check this condition better
                    if (this.query && !$event) {
                        this.queryChange.next(this.query);
                        this.query = null;
                    }
                };
                __decorate([
                    core_1.Output(), 
                    __metadata('design:type', Object)
                ], SearchComponent.prototype, "queryChange", void 0);
                SearchComponent = __decorate([
                    core_1.Component({
                        selector: "search-component",
                        template: "\n        <form class=\"navbar-form navbar-left\" role=\"search\">\n            <div class=\"form-group\">\n                <input [(ngModel)]=\"query\" (keypress)=\"search($event)\" type=\"text\" class=\"form-control\" placeholder=\"Search for music\">\n            </div>\n            <button type=\"submit\" class=\"btn btn-default\" (click)=\"search()\" [disabled]=\"!query\">Submit</button>\n        </form>\n    ",
                        providers: []
                    }), 
                    __metadata('design:paramtypes', [])
                ], SearchComponent);
                return SearchComponent;
            }());
            exports_1("SearchComponent", SearchComponent);
        }
    }
});
//# sourceMappingURL=SearchComponent.js.map
System.register(["angular2/core", "angular2/router"], function(exports_1, context_1) {
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
    var core_1, router_1;
    var WelcomeComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            }],
        execute: function() {
            WelcomeComponent = (function () {
                function WelcomeComponent(router, routeParams) {
                    this.router = router;
                    this.routeParams = routeParams;
                    if (routeParams.get('id')) {
                        alert('Im going to: ' + routeParams.get('id').replace('%20', ' '));
                        this.router.navigate([routeParams.get('id').replace('%20', ' '), {}]);
                    }
                }
                WelcomeComponent = __decorate([
                    core_1.Component({
                        selector: "welcome",
                        template: "\n        <h1>WelcomeComponent</h1>\n    ",
                        providers: []
                    }), 
                    __metadata('design:paramtypes', [router_1.Router, router_1.RouteParams])
                ], WelcomeComponent);
                return WelcomeComponent;
            }());
            exports_1("WelcomeComponent", WelcomeComponent);
        }
    }
});
//# sourceMappingURL=WelcomeComponent.js.map
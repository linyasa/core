System.register(["angular2/core", "angular2/router", './common/WelcomeComponent', './common/HeaderComponent', './common/FooterComponent', './common/Portlet1', './common/Portlet2'], function(exports_1, context_1) {
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
    var core_1, router_1, WelcomeComponent_1, HeaderComponent_1, FooterComponent_1, Portlet1_1, Portlet2_1;
    var AppComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (WelcomeComponent_1_1) {
                WelcomeComponent_1 = WelcomeComponent_1_1;
            },
            function (HeaderComponent_1_1) {
                HeaderComponent_1 = HeaderComponent_1_1;
            },
            function (FooterComponent_1_1) {
                FooterComponent_1 = FooterComponent_1_1;
            },
            function (Portlet1_1_1) {
                Portlet1_1 = Portlet1_1_1;
            },
            function (Portlet2_1_1) {
                Portlet2_1 = Portlet2_1_1;
            }],
        execute: function() {
            AppComponent = (function () {
                function AppComponent() {
                }
                AppComponent = __decorate([
                    router_1.RouteConfig([
                        { path: '/html/ng/', name: 'Welcome', component: WelcomeComponent_1.WelcomeComponent },
                        { path: '/html/ng/p/portlet1', name: 'Portlet 1', component: Portlet1_1.Portlet1 },
                        { path: '/html/ng/p/portlet2', name: 'Portlet 2', component: Portlet2_1.Portlet2 },
                    ]),
                    core_1.Component({
                        selector: "my-app",
                        template: "\n    <header-component></header-component>\n    <div class=\"container\">\n        <router-outlet></router-outlet>\n    </div>\n    <footer-component></footer-component>\n    ",
                        providers: [],
                        directives: [router_1.RouterOutlet, WelcomeComponent_1.WelcomeComponent, HeaderComponent_1.HeaderComponent, FooterComponent_1.FooterComponent]
                    }), 
                    __metadata('design:paramtypes', [])
                ], AppComponent);
                return AppComponent;
            }());
            exports_1("AppComponent", AppComponent);
        }
    }
});
//# sourceMappingURL=app.component.js.map
System.register(["angular2/core", "./services/UserService"], function(exports_1, context_1) {
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
    var core_1, UserService_1;
    var RegistrationComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (UserService_1_1) {
                UserService_1 = UserService_1_1;
            }],
        execute: function() {
            RegistrationComponent = (function () {
                function RegistrationComponent(userService) {
                    this.userService = userService;
                    this.user = {
                        userName: "",
                        email: "",
                        password: ""
                    };
                }
                RegistrationComponent.prototype.registerUser = function ($event) {
                    var _this = this;
                    $event.preventDefault();
                    this.userService.createUser(this.user).subscribe(function (user) {
                        _this.userService.login(_this.user.email, _this.user.password).subscribe(function (data) {
                        });
                    }, function (err) {
                        console.log(err);
                    });
                };
                RegistrationComponent = __decorate([
                    core_1.Component({
                        selector: "registration",
                        template: "\n        <form class=\"form-horizontal\" #registerForm=\"ngForm\" (submit)=\"registerUser($event)\">\n            <div class=\"form-group\">\n                <label for=\"\" class=\"col-sm-2 control-label\">Username</label>\n                <div class=\"col-sm-10\">\n                    <input type=\"text\" class=\"form-control\" id=\"username\" placeholder=\"Username\" [(ngModel)]=\"user.userName\" ngControl=\"userName\" #userName=\"ngForm\" />\n                </div>\n            </div>\n            <div class=\"form-group\">\n                <label for=\"\" class=\"col-sm-2 control-label\">Email</label>\n                <div class=\"col-sm-10\">\n                    <input type=\"email\" class=\"form-control\" id=\"email\" placeholder=\"Email\" [(ngModel)]=\"user.email\" ngControl=\"email\" #email=\"ngForm\" />\n                </div>\n            </div>\n            <div class=\"form-group\">\n                <label for=\"\" class=\"col-sm-2 control-label\">Password</label>\n                <div class=\"col-sm-10\">\n                    <input type=\"password\" class=\"form-control\" id=\"password\" placeholder=\"Password\" [(ngModel)]=\"user.password\" ngControl=\"password\" #password=\"ngForm\" />\n                </div>\n            </div>\n            <div class=\"form-group\">\n                <div class=\"col-sm-offset-2 col-sm-10\">\n                    <button type=\"submit\" class=\"btn btn-default\">Sign in</button>\n                </div>\n            </div>\n        </form>\n    ",
                        providers: [UserService_1.UserService]
                    }), 
                    __metadata('design:paramtypes', [UserService_1.UserService])
                ], RegistrationComponent);
                return RegistrationComponent;
            }());
            exports_1("RegistrationComponent", RegistrationComponent);
        }
    }
});
//# sourceMappingURL=registration.component.js.map
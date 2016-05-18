System.register(["angular2/core", "angular2/router", "./services/music.service", "./models/IAlbum"], function(exports_1, context_1) {
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
    var core_1, router_1, music_service_1, IAlbum_1;
    var AlbumComponent;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (router_1_1) {
                router_1 = router_1_1;
            },
            function (music_service_1_1) {
                music_service_1 = music_service_1_1;
            },
            function (IAlbum_1_1) {
                IAlbum_1 = IAlbum_1_1;
            }],
        execute: function() {
            AlbumComponent = (function () {
                function AlbumComponent(musicService, routeParams) {
                    this.musicService = musicService;
                    this.routeParams = routeParams;
                    this.imageSize = IAlbum_1.AlbumImageSize.EXTRALARGE;
                    this.getAlbum(this.routeParams.get("id"));
                }
                AlbumComponent.prototype.getAlbum = function (id) {
                    var _this = this;
                    this.musicService.getAlbum(id)
                        .subscribe(function (res) {
                        _this.album = res;
                    });
                };
                AlbumComponent = __decorate([
                    core_1.Component({
                        selector: "album",
                        template: "\n        <div class=\"row\" *ngIf=\"album\">\n            <div class=\"col-md-3\">\n                <img class=\"img-thumbnail\" [src]=\"album.getImage(imageSize)\">\n            </div>\n            <div class=\"col-md-9\">\n                <div class=\"page-header\">\n                    <h1>{{album.name}}<br /> <small>{{album.artist}}</small></h1>\n                </div>\n                <ul>\n                    <li *ngFor=\"#song of album.songs\">\n                        {{ song.name }}\n                    </li>\n                </ul>\n            </div>\n        </div>\n    ",
                        providers: [music_service_1.MusicService]
                    }), 
                    __metadata('design:paramtypes', [music_service_1.MusicService, router_1.RouteParams])
                ], AlbumComponent);
                return AlbumComponent;
            }());
            exports_1("AlbumComponent", AlbumComponent);
        }
    }
});
//# sourceMappingURL=album.component.js.map
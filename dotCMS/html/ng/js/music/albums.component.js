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
    var AlbumsComponent;
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
            AlbumsComponent = (function () {
                function AlbumsComponent(routeParams, musicService) {
                    this.routeParams = routeParams;
                    this.musicService = musicService;
                    this.params = {};
                    this.albums = [];
                    this.albumImageSize = IAlbum_1.AlbumImageSize.MEDIUM;
                }
                AlbumsComponent.prototype.ngOnInit = function () {
                    this.albumSearch(this.routeParams.get("query"));
                };
                AlbumsComponent.prototype.albumSearch = function (query) {
                    var _this = this;
                    this.musicService.searchAlbums(query)
                        .subscribe(function (res) {
                        _this.albums = res.albums;
                    });
                };
                AlbumsComponent = __decorate([
                    core_1.Component({
                        selector: "albums",
                        template: "\n        <h1>Albums</h1>\n        <ul class=\"media-list\">\n            <li class=\"media\" *ngFor=\"#album of albums\">\n                <a [routerLink]=\"['Album', { id : album.id }]\">\n                    <div class=\"media-left\">\n                        <img class=\"media-object\" [src]=\"album.getImage(albumImageSize)\">\n                    </div>\n                    <div class=\"media-body\">\n                        <h4 class=\"media-heading\">{{ album.name }}</h4>\n                        <p>{{ album.artist }}</p>\n                    </div>\n                </a>\n            </li>\n        </ul>\n    ",
                        directives: [router_1.RouterLink],
                        providers: [music_service_1.MusicService]
                    }), 
                    __metadata('design:paramtypes', [router_1.RouteParams, music_service_1.MusicService])
                ], AlbumsComponent);
                return AlbumsComponent;
            }());
            exports_1("AlbumsComponent", AlbumsComponent);
        }
    }
});
//# sourceMappingURL=albums.component.js.map
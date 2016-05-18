System.register(["angular2/core", "rxjs/Observable", "rxjs/add/operator/map", "angular2/http", "../models/Album", "../models/Song"], function(exports_1, context_1) {
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
    var core_1, Observable_1, http_1, Album_1, Song_1;
    var MusicService;
    return {
        setters:[
            function (core_1_1) {
                core_1 = core_1_1;
            },
            function (Observable_1_1) {
                Observable_1 = Observable_1_1;
            },
            function (_1) {},
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (Album_1_1) {
                Album_1 = Album_1_1;
            },
            function (Song_1_1) {
                Song_1 = Song_1_1;
            }],
        execute: function() {
            // Application name	MyTunes
            // API key	35861908a853b478466d80563e06b52f
            // Shared secret	ffe024e62c128b413c6af5fc4d9a1d21
            // Registered to	fmontes
            MusicService = (function () {
                function MusicService(http) {
                    this.http = http;
                    this.config = {
                        musicApi: {
                            url: "http://ws.audioscrobbler.com/2.0/",
                            key: "35861908a853b478466d80563e06b52f"
                        }
                    };
                }
                MusicService.prototype.getUrl = function (type) {
                    var types = {
                        search: "album.search",
                        album: "album.getInfo"
                    };
                    return this.config.musicApi.url + "?format=json&method=" + types[type] + "&api_key=" + this.config.musicApi.key;
                };
                MusicService.prototype.searchAlbums = function (query) {
                    var _this = this;
                    return new Observable_1.Observable(function (observable) {
                        var url = _this.getUrl("search") + "&album=" + query;
                        _this.http.get(url)
                            .map(function (res) {
                            res = res.json();
                            var results = res.results;
                            var albums = [];
                            results.albummatches.album.forEach(function (data) {
                                // Some results come without mbid
                                if (data.mbid) {
                                    albums.push(new Album_1.Album(data.mbid, data.name, data.artist, data.url, data.image));
                                }
                            });
                            return albums;
                        })
                            .subscribe(function (res) {
                            observable.next({
                                albums: res
                            });
                        });
                    });
                };
                MusicService.prototype.getAlbum = function (id) {
                    var _this = this;
                    return new Observable_1.Observable(function (observable) {
                        var url = _this.getUrl("album") + "&mbid=" + id;
                        _this.http.get(url)
                            .map(function (res) {
                            res = res.json().album;
                            var songs = [];
                            res.tracks.track.forEach(function (song) {
                                songs.push(new Song_1.Song(song.name));
                            });
                            var album = new Album_1.Album(res.mbid, res.name, res.artist, res.url, res.image, songs);
                            return album;
                        })
                            .subscribe(function (res) {
                            observable.next(res);
                        });
                    });
                };
                MusicService = __decorate([
                    core_1.Injectable(), 
                    __metadata('design:paramtypes', [http_1.Http])
                ], MusicService);
                return MusicService;
            }());
            exports_1("MusicService", MusicService);
        }
    }
});
//# sourceMappingURL=music.service.js.map
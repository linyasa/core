import {Injectable} from 'angular2/core';
import {Http} from 'angular2/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';


@Injectable()
export class HelloService {
    private helloWorldApiURL:string = "http://localhost:8080/api/angular/hello";
    
    constructor(private _http:Http) {}
    
    getHelloURL() {
        return this.helloWorldApiURL;
    }
    
    getHello() {
        return new Observable(observable => {
            this._http.get(this.getHelloURL())
            .map(res => res.json())
            .subscribe(res => {
                    if (res.code == "404") {
                        observable.error(res.message);
                    } else {
                        var message = res.result;
                        observable.next(message);
                    }
            }); 
        });
    }
}
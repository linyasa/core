import {Component} from "angular2/core";
import {HTTP_PROVIDERS, Http} from 'angular2/http';

@Component({
    selector: "portlet1",
    template: `
        <h1>Porlet 1</h1>
        <br>
        <h1>This response is coming from a RESTful resource (/api/jwt/poc1): {{poc1Response?.Success}}</h1>
    `,
    providers: [HTTP_PROVIDERS]
})

export class Portlet1 {
    poc1Response:any;
    constructor(http:Http) {
        http.get('/api/jwt/poc1').subscribe(res => {
            this.poc1Response = res.json();
        });
    }
}
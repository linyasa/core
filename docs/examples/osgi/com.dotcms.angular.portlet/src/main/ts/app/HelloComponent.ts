import {Component} from "angular2/core";
import {HelloService} from './service/HelloService';


@Component({
    selector: "my-app",
    template: `
   <div class="container">
        <h2>Hello {{message}}</h2>
    </div>    
    `,
    styleUrls : ["/osgi/com.dotcms.angular.portlet-0.1/ng/css/main.css"],
    providers: [HelloService]
})
export class HelloComponent { 
    public message:string="";
    
    constructor(private _helloService:HelloService) {
        this.getHelloMesage();
    }
    
     getHelloMesage= function() {
       this.message = this._helloService.getHello()
                .subscribe(res => {
                        this.message=res;
                    }, error => {
                    this.message = error;
                    });
    }
}

import {Component} from "angular2/core";
import {Router, RouterLink} from "angular2/router";
import {Http, HTTP_PROVIDERS} from 'angular2/http'

@Component({
    selector: "header-component",
    styles: [`
        .t1 {
          font-size: 12px;
        }
        .t2{
        font-size: 10px;
        }
    `],
    template: `
    <div class="container">
        <span *ngFor="#menu of menus" style="display: inline; float: left;">
            <a href="{{menu.url}}"><label class="t1">{{menu.tabName}}</label></a>
            <ul>
                <li *ngFor="#menuItem of menu.menuItems">
                <a href="{{menuItem.url}}" *ngIf="!menuItem.angular">
                    <label class="t2">{{menuItem.name}}</label>
                </a>
                <a [routerLink]="[menuItem.id]" *ngIf="menuItem.angular">
                    <label class="t2">{{menuItem.name}}</label>
                </a>
                </li>
            </ul>
        </span>
    </div>
    `,
    providers: [[HTTP_PROVIDERS]],
    directives: [RouterLink]
})

export class HeaderComponent {

    private menus:any[];

    constructor(private http: Http,private router:Router) {
    }

    ngOnInit(){

        /*this.menus = [{
         tabName: 'T1',
         tabDescription: 'AAAAA',
         url: 'http://localhost:8080/aaaaa',
         menuItems: [
         {
         name: 'T11',
         url: 'http://localhost:8080/bbbbb'
         },
         {
         name: 'T12',
         url: 'http://localhost:8080/bbbbb'
         },
         {
         name: 'ANGULAR_PORTLET3',
         url: 'http://localhost:8080/bbbbb'
         }
         ]
         },
         {
         tabName: 'T2',
         tabDescription: 'BBB',
         url: 'http://localhost:8080/bbbb',
         menuItems: [
         {
         name: 'T11',
         url: '/bbbbb'
         }
         ]
         }];*/

        this.http.get('/api/core_web/menu')
            .subscribe(res => this.menus = res.json());

    }
}
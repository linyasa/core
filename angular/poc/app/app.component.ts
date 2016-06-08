import {Component} from "angular2/core";
import {RouteConfig, RouterOutlet} from "angular2/router";
import {WelcomeComponent} from './common/WelcomeComponent';
import {HeaderComponent} from './common/HeaderComponent';
import {FooterComponent} from './common/FooterComponent';
import {Portlet1} from './common/Portlet1';
import {Portlet2} from './common/Portlet2';

@RouteConfig([
    {path:'/html/ng/', name: 'Welcome', component: WelcomeComponent},
    {path:'/html/ng/p/ANGULAR_PORTLET3', name: 'ANGULAR_PORTLET3', component: Portlet1},
    {path:'/html/ng/p/ANGULAR_PORTLET4', name: 'ANGULAR_PORTLET4', component: Portlet2},
])

@Component({
    selector: "my-app",
    template: `
    <header-component></header-component>
    <div class="container">
        <router-outlet></router-outlet>
    </div>
    <footer-component></footer-component>
    `,
    providers: [],
    directives: [RouterOutlet, WelcomeComponent, HeaderComponent, FooterComponent]
})

export class AppComponent {
    constructor() {
    }
}
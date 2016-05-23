import {Component} from "angular2/core";
import {Router, RouterLink, RouteParams} from "angular2/router";

@Component({
    selector: "welcome",
    template: `
        <h1>WelcomeComponent</h1>
    `,
    providers: []
})

export class WelcomeComponent {
    constructor(private router:Router, private routeParams:RouteParams) {
      if (routeParams.get('id')) {
        alert('Im going to: ' + routeParams.get('id').replace('%20', ' '));
        this.router.navigate([routeParams.get('id').replace('%20', ' '), {}])
      }
    }
}

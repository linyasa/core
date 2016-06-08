import {Component} from "angular2/core";

@Component({
    selector: "footer-component",
    template: `
        <footer>
            <div class="container">
                <p>Hello I am the footer</p>
            </div>
        </footer>
    `,
    providers: []
})

export class FooterComponent {
    constructor() {
    }
}
import {Component, Output, EventEmitter} from "angular2/core";

@Component({
    selector: "search-component",
    template: `
        <form class="navbar-form navbar-left" role="search">
            <div class="form-group">
                <input [(ngModel)]="query" (keypress)="search($event)" type="text" class="form-control" placeholder="Search for music">
            </div>
            <button type="submit" class="btn btn-default" (click)="search()" [disabled]="!query">Submit</button>
        </form>
    `,
    providers: []
})

export class SearchComponent {
    @Output() queryChange = new EventEmitter();
    
    private query:string = null;
    
    constructor() {}
    
    search($event?:KeyboardEvent) {
        // TODO: check this condition better
        if (this.query && !$event) {
            this.queryChange.next(this.query);
            this.query = null;
        }
    }
}
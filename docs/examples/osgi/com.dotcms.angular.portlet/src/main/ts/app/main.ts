import {bootstrap} from 'angular2/platform/browser';
import {HTTP_PROVIDERS} from 'angular2/http';
import {HelloComponent} from './HelloComponent';

bootstrap(<any>HelloComponent, [HTTP_PROVIDERS])

//our root app component
import {Component} from '@angular/core'

@Component({
  selector: 'my-app',
  providers: [],
  // this is the workflow portlet
  //templateUrl: 'http://localhost:8080/c/portal/layout?p_l_id=34885ddb-3537-4a79-a02c-0550c5087d5c&p_p_id=EXT_21&p_p_action=0&&dm_rlout=1&r=1463334219159',
  template: `
    <h1>{{name}}</h1>
    <div id="content"></div>
  `,
  directives: []
})
export class App {
  constructor() {
    this.name = 'Angular2 (Release Candidate!)'

    var myRequest = new XMLHttpRequest();
    myRequest.open('GET', '/c/portal/layout?p_l_id=34885ddb-3537-4a79-a02c-0550c5087d5c&p_p_id=EXT_21&p_p_action=0&&dm_rlout=1&r=1463334219159')
    myRequest.onreadystatechange = function() {
      if (myRequest.readyState == XMLHttpRequest.DONE) {
        var content = document.getElementById("content");
        content.innerHTML = myRequest.responseText;

        var someScripts = document.getElementById("bd").getElementsByTagName("script");

        for (var i = 0; i < someScripts.length; i++) {
          if (someScripts[i].text.length) {
            eval(someScripts[i].text);
          }
        }
        //dojo.parser.parse(content)
      }
    }
    myRequest.send();


    // var paintThis = window.setInterval(function() {
    //   console.log('paintThis')
    //   var body = document.getElementsByTagName('body')[1];
    //   if (body) {
    //     clearInterval(paintThis);
    //     dojo.parser.parse(body)
    //     body.style.visibility = 'visible';
    //     var panel = dijit.byId('borderContainer').domNode;
    //     panel.style.height = "700px";
    //     panel.children[0].style.height = "100%";
    //     panel.children[1].style.height = "100%";
    //   }
    // }, 1000);
  }
}

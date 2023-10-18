import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'ReviziaBankFrontend';
    protected readonly window = window;

    logout() {
        this.window.alert("Funcionalidade não implementada.")
    }
}

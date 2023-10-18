import {ErrorHandler, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import {environment} from "../environments/environment";
import {MatToolbarModule} from "@angular/material/toolbar";
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {MatButtonModule} from "@angular/material/button";
import { ListaContasComponent } from './components/conta/lista-contas/lista-contas.component';
import { NovaContaComponent } from './components/conta/nova-conta/nova-conta.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found/page-not-found.component';
import {MatCardModule} from "@angular/material/card";
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {GlobalErrorHandler} from "./core/error-handling/global-error-handler";
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {HttpClientModule} from "@angular/common/http";
import {MatTableModule} from "@angular/material/table";
import { TransferirComponent } from './components/transacao/transferir/transferir/transferir.component';
import { AsyncValidadorContaExistsDirective } from './shared/validators/async-validador-conta-exists.directive';
import { ExtratoComponent } from './components/transacao/extrato/extrato.component';
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";

@NgModule({
  declarations: [
    AppComponent,
    ListaContasComponent,
    NovaContaComponent,
    PageNotFoundComponent,
    TransferirComponent,
    AsyncValidadorContaExistsDirective,
    ExtratoComponent
  ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        MatToolbarModule,
        RouterOutlet,
        MatButtonModule,
        RouterLink,
        RouterLinkActive,
        MatCardModule,
        ReactiveFormsModule,
        MatInputModule,
        MatIconModule,
        MatSnackBarModule,
        HttpClientModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule
    ],
  providers: [
    { provide: "BASE_API_URL", useValue: environment.apiUrl },
    { provide: ErrorHandler, useClass: GlobalErrorHandler },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

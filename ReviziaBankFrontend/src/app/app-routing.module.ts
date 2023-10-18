import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {AppComponent} from "./app.component";
import {ListaContasComponent} from "./components/conta/lista-contas/lista-contas.component";
import {NovaContaComponent} from "./components/conta/nova-conta/nova-conta.component";
import {PageNotFoundComponent} from "./components/page-not-found/page-not-found/page-not-found.component";
import {TransferirComponent} from "./components/transacao/transferir/transferir/transferir.component";
import {ExtratoComponent} from "./components/transacao/extrato/extrato.component";



const appRoutes: Routes = [
  { path: 'contas', component: ListaContasComponent },
  { path: 'criar-nova-conta',        component: NovaContaComponent },
  { path: 'trasferir-valores',        component: TransferirComponent },
  { path: 'extrato/:numeroConta',        component: ExtratoComponent },
  { path: '',   redirectTo: '/contas', pathMatch: 'full' },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  imports: [
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // <-- debugging purposes only
    )
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {}

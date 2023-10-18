import { Component } from '@angular/core';
import {Conta} from "../../../shared/models/conta";
import {ContaService} from "../../../shared/services/conta/conta.service";
import {ServiceError} from "../../../shared/models/service-error";

@Component({
  selector: 'app-lista-contas',
  templateUrl: './lista-contas.component.html',
  styleUrls: ['./lista-contas.component.css']
})
export class ListaContasComponent {
  dataSource: Conta[] = [];
  displayedColumns: string[] = ['id', 'titular', 'numeroDaConta', 'saldo'];

  constructor(private contaService: ContaService) { }

  ngOnInit(): void {
    this.contaService.listarContas().subscribe(data => {
      if (Array.isArray(data)) {
        this.dataSource = data;
      }
    });
  }
}

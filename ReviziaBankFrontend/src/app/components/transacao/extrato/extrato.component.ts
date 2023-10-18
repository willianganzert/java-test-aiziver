import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort} from "@angular/material/sort";
import {TransacaoService} from "../../../shared/services/transacao/transacao.service";
import {MatTableDataSource} from "@angular/material/table";
import {Transacao} from "../../../shared/models/transacao";
import {ServiceError} from "../../../shared/models/service-error";

@Component({
  selector: 'app-extrato',
  templateUrl: './extrato.component.html',
  styleUrls: ['./extrato.component.css']
})
export class ExtratoComponent implements OnInit{
  transacoes!: Transacao[];
  displayedColumns: string[] = ['transacaoId', "contaOrigem", "contaDestino", "valor", "dataHora" ];
  numeroDaConta!: string;
  titular: string = '';

  constructor(private route: ActivatedRoute,private transacaoService: TransacaoService) {}
  ngOnInit(): void {
    this.numeroDaConta = this.route.snapshot.paramMap.get('numeroConta')!;
    this.titular = 'Nome do titular';
    if(this.numeroDaConta != null) {
        this.transacaoService.obterExtrato(this.numeroDaConta).subscribe(
            (data: Transacao[] | ServiceError) => {
                if (data instanceof Array) {
                    this.transacoes = data;
                }
            }
        )
    }
  }



}

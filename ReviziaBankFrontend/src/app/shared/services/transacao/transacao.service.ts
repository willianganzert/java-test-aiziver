import {Inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {TransferenciaRequest} from "../../models/transferencia-request";
import {Observable} from "rxjs";
import {ServiceError} from "../../models/service-error";
import {Transacao} from "../../models/transacao";

@Injectable({
  providedIn: 'root'
})
export class TransacaoService {

  private baseUrl: string;

  constructor(private http: HttpClient, @Inject('BASE_API_URL') baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  transferir(request: TransferenciaRequest): Observable<void | ServiceError> {

    return this.http.post<void>(`${this.baseUrl}/v1/transacoes/transferir`, request)
  }

  obterExtrato(numeroDaConta: string): Observable<Transacao[] | ServiceError> {
    return this.http.get<Transacao[]>(`${this.baseUrl}/v1/transacoes/extrato/${numeroDaConta}`);
  }

}

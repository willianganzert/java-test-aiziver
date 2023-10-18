import {Inject, Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Conta} from "../../models/conta";
import {ServiceError} from "../../models/service-error";


@Injectable({
  providedIn: 'root'
})
export class ContaService {

  private baseUrl: string;

  constructor(private http: HttpClient, @Inject('BASE_API_URL') baseUrl: string) {
    this.baseUrl = baseUrl;
  }

  listarContas(): Observable<Conta[] | ServiceError> {
    return this.http.get<Conta[]>(`${this.baseUrl}/v1/contas`)
  }

  criarConta(conta: Conta): Observable<HttpResponse<Conta> | ServiceError> {
    return this.http.post<Conta>(`${this.baseUrl}/v1/contas`, conta, { observe: 'response' })
  }

  contaExists(value: string) {
    return this.http.get<boolean>(`${this.baseUrl}/v1/contas/exists/${value}`)
  }
}

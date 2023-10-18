export interface Transacao {
  transacaoId?: number;
  contaOrigem: string;
  contaDestino: string;
  valor: number;
  dataHora?: string;
}

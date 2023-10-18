import {Injectable, NgZone} from '@angular/core';
import {HttpErrorResponse} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ServiceError} from "../../shared/models/service-error";

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlingService {

  constructor(private snackBar: MatSnackBar, private zone: NgZone) {}

  public handleError(error: HttpErrorResponse): Observable<never> {
    this.showErrorMessage(error);

    return throwError(() => new Error('Ocorreu um erro. Por favor, tente novamente mais tarde.'));
  }

  private showErrorMessage(error: HttpErrorResponse): void {
    let message = 'Erro desconhecido. Por favor, tente novamente mais tarde.';

    if (error.error instanceof ErrorEvent) {
      message = error.error.message;
    } else {
      if(!error.error.message) {
        switch (error.status) {
          case 0: // Unknown
            message = 'Por favor, verifique sua conexão com a internet.';
            break;
          case 400: // BadRequest
            message = 'Erro de validação. Por favor, verifique os campos e tente novamente.';
            break;
          case 409: // Conflict
            message = 'Conflito! Já existe uma conta com esse número.';
            break;
          case 500: // InternalServerError
            message = 'Ocorreu um erro no servidor. Por favor, contate o administrador do sistema.';
            break;
          default:
            message = 'Ocorreu um erro desconhecido. Por favor, tente novamente.';
            break;
        }
      } else {
        const serviceError = error.error as ServiceError;
        message = serviceError.message;
      }
    }

    // Usando NgZone para garantir que o snackbar seja mostrado corretamente
    this.zone.run(() => {
      this.snackBar.open(message, 'Close', {
        duration: 5000,
      });
    });
  }
}

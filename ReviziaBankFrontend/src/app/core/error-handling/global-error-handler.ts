import {ErrorHandler, Injectable} from "@angular/core";
import {ErrorHandlingService} from "./error-handling.service";

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

  constructor(private errorHandlingService: ErrorHandlingService) {}

  handleError(error: any): void {
    this.errorHandlingService.handleError(error);
  }
}

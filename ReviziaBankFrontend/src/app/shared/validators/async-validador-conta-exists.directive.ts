import {Directive, Injectable} from '@angular/core';
import {AbstractControl, AsyncValidator, NG_ASYNC_VALIDATORS, ValidationErrors} from "@angular/forms";
import {debounceTime, first, map, Observable, of, switchMap} from "rxjs";
import {ContaService} from "../services/conta/conta.service";


@Injectable({providedIn: 'root'})
export class AsyncValidadorContaExists implements AsyncValidator {
  constructor(private contaService: ContaService) {
  }

  validate(
      control: AbstractControl
  ): Observable<ValidationErrors | null> {
    return of(control.value).pipe(
        debounceTime(500),
        switchMap((value: string) => this.contaService.contaExists(value).pipe(
            map(exists => !exists ? {contaInvalida: true} : null))),
        first());
  }
}

@Directive({
  selector: '[appAsyncValidadorContaExists]',
  providers: [{
    provide: NG_ASYNC_VALIDATORS, useExisting: AsyncValidadorContaExistsDirective, multi:
        true
  }]
})
export class AsyncValidadorContaExistsDirective {

  constructor(private validator: AsyncValidadorContaExists) {
  }

  validate(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.validator.validate(control);
  }
}

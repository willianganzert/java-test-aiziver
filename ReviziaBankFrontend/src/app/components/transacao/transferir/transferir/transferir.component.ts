import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {Conta} from "../../../../shared/models/conta";
import {ContaService} from "../../../../shared/services/conta/conta.service";
import {isHttpResponse} from "../../../../shared/utils/type-guards";
import {TransacaoService} from "../../../../shared/services/transacao/transacao.service";
import {
  AsyncValidadorContaExists,
  AsyncValidadorContaExistsDirective
} from "../../../../shared/validators/async-validador-conta-exists.directive";

@Component({
  selector: 'app-transferir',
  templateUrl: './transferir.component.html',
  styleUrls: ['./transferir.component.css']
})
export class TransferirComponent implements OnInit {
  transferenciaRealizadaComSucesso: boolean = false;
  transferenciaForm!: FormGroup;

  constructor(private fb: FormBuilder, private transacaoService:TransacaoService, private contaExistsValidator:AsyncValidadorContaExists) { }

  get contaOrigem() {
    return this.transferenciaForm.get('contaOrigem')!;
  }

  get contaDestino() {
    return this.transferenciaForm.get('contaDestino')!;
  }

  get valor() {
    return this.transferenciaForm.get('valor')!;
  }

  differentContaValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: boolean } | null => {
      const contaOrigem = control.get('contaOrigem');
      const contaDestino = control.get('contaDestino');

      if (contaOrigem && contaDestino && contaOrigem.value === contaDestino.value) {
        return { 'sameConta': true };
      }
      return null;
    };
  }

  ngOnInit(): void {
    this.initForm();
  }
  initForm() {
    this.transferenciaForm = this.fb.group({
      contaOrigem: new FormControl('', {
        validators: [
          Validators.required,
          Validators.pattern('^[0-9]{5,10}$')
        ],
        asyncValidators: [this.contaExistsValidator.validate.bind(this.contaExistsValidator)],
        updateOn: 'blur'
      }),
      contaDestino: new FormControl('', {
        validators: [
          Validators.required,
          Validators.pattern('^[0-9]{5,10}$')
        ],
        asyncValidators: [this.contaExistsValidator.validate.bind(this.contaExistsValidator)],
        updateOn: 'blur'
      }),
      valor: [
        '',
        [
          Validators.required,
          Validators.min(0.0)
        ]
      ]
    }, {validators: this.differentContaValidator()});
  }


  transferir() {
    if (this.transferenciaForm.valid) {
      let payload = this.transferenciaForm.getRawValue();

      this.transacaoService.transferir(payload).subscribe({
        next: response => {
          this.transferenciaRealizadaComSucesso = true;
        }
      });
    }
  }


  getErrorMessage(control: AbstractControl): string {
    if (control.hasError('required')) {
      return 'Este campo é obrigatório';
    } else if (control.hasError('minLength')) {
      return `Deve ter pelo menos ${control.errors?.['minLength'].requiredLength} caracteres`;
    } else if (control.hasError('maxLength')) {
      return `Devem ter no máximo ${control.errors?.['maxLength'].requiredLength} caracteres`;
    } else if (control.hasError('pattern')) {
      return `Padrão inválido (${control.errors?.['pattern'].requiredPattern})`;
    } else if (control.hasError('contaInvalida')) {
      return `Conta inexistente ou inválida`;
    }

    return '';
  }

}

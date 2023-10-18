import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {ContaService} from "../../../shared/services/conta/conta.service";
import {ServiceError} from "../../../shared/models/service-error";
import {Conta} from "../../../shared/models/conta";
import {isHttpResponse} from "../../../shared/utils/type-guards";

@Component({
  selector: 'app-nova-conta',
  templateUrl: './nova-conta.component.html',
  styleUrls: ['./nova-conta.component.css']
})
export class NovaContaComponent implements OnInit {
  contaCriadaComSucesso: boolean = false;
  newResouceLocation: string = '';
  contaForm!: FormGroup;
  createdResource: Conta | undefined

  constructor(private fb: FormBuilder, private contaService:ContaService) { }

  get titular() {
    return this.contaForm.get('titular')!;
  }

  get saldo() {
    return this.contaForm.get('saldo')!;
  }

  get numeroDaConta() {
    return this.contaForm.get('numeroDaConta')!;
  }

  ngOnInit(): void {
    this.initForm();
  }

  initForm() {
    this.contaForm = this.fb.group({
      titular: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(100)
        ]
      ],
      numeroDaConta: [
        '',
        [
          Validators.required,
          Validators.pattern('^[0-9]{5,10}$')
        ]
      ],
      saldo: [
        '',
        [
          Validators.required,
          Validators.min(0.0)
        ]
      ]
    });
  }


  criarConta() {
    if (this.contaForm.valid) {
      let payload = this.contaForm.getRawValue();

      this.contaService.criarConta(payload).subscribe({
        next: response => {
          if (isHttpResponse(response)) {
            this.newResouceLocation = response.headers.get('Location')!;
            this.contaCriadaComSucesso = true;
            this.createdResource = response.body!;
          }
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
    }
    return '';
  }

}

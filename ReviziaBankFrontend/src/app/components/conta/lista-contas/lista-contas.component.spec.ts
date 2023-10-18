import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaContasComponent } from './lista-contas.component';

describe('ListaContasComponent', () => {
  let component: ListaContasComponent;
  let fixture: ComponentFixture<ListaContasComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListaContasComponent]
    });
    fixture = TestBed.createComponent(ListaContasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

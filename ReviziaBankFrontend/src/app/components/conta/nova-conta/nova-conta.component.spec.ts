import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NovaContaComponent } from './nova-conta.component';

describe('NovaContaComponent', () => {
  let component: NovaContaComponent;
  let fixture: ComponentFixture<NovaContaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NovaContaComponent]
    });
    fixture = TestBed.createComponent(NovaContaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

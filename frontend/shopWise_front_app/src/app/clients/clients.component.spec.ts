import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { ClientsComponent } from './clients.component';
import { ClientService } from '../services/client-service';

describe('ClientsComponent', () => {
  let service: jasmine.SpyObj<ClientService>;

  beforeEach(async () => {
    service = jasmine.createSpyObj('ClientService', ['listerParBoutique']);
    await TestBed.configureTestingModule({
      imports: [ClientsComponent],
      providers: [{ provide: ClientService, useValue: service }]
    }).compileComponents();
  });

  it('affiche les clients de la boutique', () => {
    service.listerParBoutique.and.returnValue(of([{ id: 4, name: 'Marie', email: 'm@s.fr', phone: null, solde_points: 10 }]));
    const fixture = TestBed.createComponent(ClientsComponent);
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('Marie');
  });

  it('affiche une erreur si la boutique est inconnue', () => {
    service.listerParBoutique.and.returnValue(throwError(() => ({ status: 404 })));
    const fixture = TestBed.createComponent(ClientsComponent);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.erreur').textContent).toContain('introuvable');
    expect(fixture.nativeElement.textContent).toContain('Aucun client');
  });
});

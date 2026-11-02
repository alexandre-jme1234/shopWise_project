import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { EspaceClientComponent } from './espace-client.component';
import { ClientService } from '../services/client-service';

describe('EspaceClientComponent', () => {
  let fixture: ComponentFixture<EspaceClientComponent>;
  let component: EspaceClientComponent;
  let service: jasmine.SpyObj<ClientService>;
  const marie = { id: 4, name: 'Marie', email: 'm@s.fr', phone: null, solde_points: 25 };

  beforeEach(async () => {
    service = jasmine.createSpyObj('ClientService', ['connecter', 'consulterPoints']);
    await TestBed.configureTestingModule({
      imports: [EspaceClientComponent],
      providers: [{ provide: ClientService, useValue: service }]
    }).compileComponents();
    fixture = TestBed.createComponent(EspaceClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('affiche le formulaire de connexion tant que le client n\'est pas connecté', () => {
    expect(fixture.nativeElement.querySelector('input[type=password]')).toBeTruthy();
  });

  it('connexion réussie : affiche le solde et l\'historique, oublie le mot de passe', () => {
    service.connecter.and.returnValue(of(marie));
    service.consulterPoints.and.returnValue(of({ client_id: 4, solde_points: 25, transactions: [
      { id: 2, type: 'UTILISATION', points: 30, date_heure: '2026-10-12T18:00:00', rendez_vous_id: null },
      { id: 1, type: 'GAIN', points: 55, date_heure: '2026-09-02T10:00:00', rendez_vous_id: 1 }
    ] }));
    component.email = 'm@s.fr';
    component.password = 'secret123';

    component.connecter();
    fixture.detectChanges();

    expect(service.consulterPoints).toHaveBeenCalledWith(4);
    expect(component.password).toBe('');
    expect(fixture.nativeElement.textContent).toContain('Solde : 25 points');
    expect(fixture.nativeElement.querySelectorAll('tbody tr').length).toBe(2);
    expect(fixture.nativeElement.textContent).toContain('−30');
    expect(fixture.nativeElement.textContent).toContain('+55');
  });

  it('connexion refusée (401) : message d\'erreur, toujours le formulaire', () => {
    service.connecter.and.returnValue(throwError(() => ({ status: 401 })));
    component.connecter();
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.erreur').textContent).toContain('incorrect');
    expect(service.consulterPoints).not.toHaveBeenCalled();
  });

  it('points indisponibles : message d\'erreur', () => {
    service.connecter.and.returnValue(of(marie));
    service.consulterPoints.and.returnValue(throwError(() => ({ status: 500 })));
    component.connecter();
    expect(component.erreur).toContain('points');
  });

  it('déconnexion : revient au formulaire', () => {
    component.client = marie;
    component.deconnecter();
    fixture.detectChanges();
    expect(component.client).toBeNull();
    expect(fixture.nativeElement.querySelector('input[type=password]')).toBeTruthy();
  });
});

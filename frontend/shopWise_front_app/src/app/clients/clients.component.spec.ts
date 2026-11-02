import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { ClientsComponent } from './clients.component';
import { ClientService } from '../services/client-service';

describe('ClientsComponent', () => {
  let service: jasmine.SpyObj<ClientService>;

  beforeEach(async () => {
    service = jasmine.createSpyObj('ClientService', ['listerParBoutique', 'creer', 'modifier']);
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

  it('crée un client puis recharge la liste', () => {
    service.listerParBoutique.and.returnValue(of([]));
    service.creer.and.returnValue(of({ id: 9, name: 'Léo', email: 'l@s.fr', phone: null, solde_points: 0 }));
    const fixture = TestBed.createComponent(ClientsComponent);
    const c = fixture.componentInstance;
    fixture.detectChanges();
    c.formulaire = { name: 'Léo', email: 'l@s.fr', phone: '', password: 'secret123' };
    c.enregistrer();
    expect(service.creer).toHaveBeenCalledWith(1, jasmine.objectContaining({ password: 'secret123' }));
    expect(c.message).toContain('Léo');
    expect(service.listerParBoutique).toHaveBeenCalledTimes(2);
  });

  it('modifie un client existant sans envoyer de mot de passe', () => {
    const leo = { id: 9, name: 'Léo', email: 'l@s.fr', phone: null, solde_points: 0 };
    service.listerParBoutique.and.returnValue(of([leo]));
    service.modifier.and.returnValue(of({ ...leo, phone: '07' }));
    const fixture = TestBed.createComponent(ClientsComponent);
    const c = fixture.componentInstance;
    fixture.detectChanges();
    c.modifier(leo);
    expect(c.formulaire.phone).toBe('');
    c.formulaire.phone = '07';
    c.enregistrer();
    expect(service.modifier).toHaveBeenCalledWith(1, 9, { name: 'Léo', email: 'l@s.fr', phone: '07' });
    expect(c.enEdition).toBeNull();
  });

  it('affiche une erreur si l\'enregistrement échoue', () => {
    service.listerParBoutique.and.returnValue(of([]));
    service.modifier.and.returnValue(throwError(() => ({ status: 404 })));
    const fixture = TestBed.createComponent(ClientsComponent);
    const c = fixture.componentInstance;
    fixture.detectChanges();
    c.modifier({ id: 9, name: 'Léo', email: 'l@s.fr', phone: '06', solde_points: 0 });
    c.enregistrer();
    expect(c.erreur).toContain('impossible');
  });
});

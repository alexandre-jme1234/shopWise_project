import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { RendezVousComponent } from './rendez-vous.component';
import { RendezVousService } from '../services/rendez-vous-service';
import { RendezVous } from '../models/rendez-vous';

describe('RendezVousComponent', () => {
  let fixture: ComponentFixture<RendezVousComponent>;
  let component: RendezVousComponent;
  let service: jasmine.SpyObj<RendezVousService>;   // ≈ @Mock de Mockito
  const planifie: RendezVous = { id: 1, client_id: 4, service_id: 1, statut: 'PLANIFIE', date_heure: '2026-11-02T10:00:00' };
  const honore: RendezVous = { ...planifie, id: 2, statut: 'HONORE' };

  beforeEach(async () => {
    service = jasmine.createSpyObj('RendezVousService', ['lister', 'honorer', 'annuler', 'creer']);
    service.lister.and.returnValue(of([planifie, honore]));      // ≈ when(...).thenReturn(...)
    await TestBed.configureTestingModule({
      imports: [RendezVousComponent],
      providers: [{ provide: RendezVousService, useValue: service }]
    }).compileComponents();
    fixture = TestBed.createComponent(RendezVousComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();                                       // déclenche ngOnInit
  });

  it('charge les rendez-vous de la boutique 1 à l\'ouverture', () => {
    expect(service.lister).toHaveBeenCalledWith(1, jasmine.objectContaining({ statut: '' }));
    const lignes = fixture.nativeElement.querySelectorAll('tbody tr');
    expect(lignes.length).toBe(2);
  });

  it('affiche le bouton Honorer / Annuler seulement pour un rendez-vous planifié', () => {
    const boutons = fixture.nativeElement.querySelectorAll('tbody button');
    expect(boutons.length).toBe(2);   // Honorer + Annuler, sur le seul rendez-vous planifié
  });

  it('honorer appelle le service puis recharge la liste', () => {
    service.honorer.and.returnValue(of({ ...planifie, statut: 'HONORE' }));
    component.honorer(planifie);
    expect(service.honorer).toHaveBeenCalledWith(1, 1);
    expect(service.lister).toHaveBeenCalledTimes(2);
  });

  it('affiche une erreur si honorer échoue (409)', () => {
    service.honorer.and.returnValue(throwError(() => ({ status: 409 })));
    component.honorer(planifie);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.erreur').textContent).toContain('ne peut pas être honoré');
  });

  it('affiche une erreur et "Aucun rendez-vous" si le chargement échoue', () => {
    service.lister.and.returnValue(throwError(() => ({ status: 404 })));
    component.charger();
    component.rendezVous = [];
    fixture.detectChanges();
    expect(component.erreur).toContain('Impossible');
    expect(fixture.nativeElement.textContent).toContain('Aucun rendez-vous');
  });

  it('annuler appelle le service puis recharge la liste', () => {
    service.annuler.and.returnValue(of({ ...planifie, statut: 'ANNULE' }));
    component.annuler(planifie);
    expect(service.annuler).toHaveBeenCalledWith(1, 1);
    expect(service.lister).toHaveBeenCalledTimes(2);
  });

  it('affiche une erreur si annuler échoue (409)', () => {
    service.annuler.and.returnValue(throwError(() => ({ status: 409 })));
    component.annuler(planifie);
    expect(component.erreur).toContain('ne peut pas être annulé');
  });

  it('crée un rendez-vous puis recharge la liste', () => {
    service.creer.and.returnValue(of({ ...planifie, id: 30 }));
    component.nouveau = { client_id: 4, service_id: 1, date_heure: '2099-01-01T10:00' };
    component.creer();
    expect(service.creer).toHaveBeenCalledWith(1, { client_id: 4, service_id: 1, date_heure: '2099-01-01T10:00' });
    expect(service.lister).toHaveBeenCalledTimes(2);
    expect(component.nouveau.client_id).toBeNull();
  });

  it('refuse la création si un champ manque', () => {
    component.nouveau = { client_id: 4, service_id: null, date_heure: '' };
    component.creer();
    expect(service.creer).not.toHaveBeenCalled();
    expect(component.erreur).toContain('obligatoires');
  });

  it('affiche une erreur si la création échoue', () => {
    service.creer.and.returnValue(throwError(() => ({ status: 400 })));
    component.nouveau = { client_id: 4, service_id: 1, date_heure: '2000-01-01T10:00' };
    component.creer();
    expect(component.erreur).toContain('Création impossible');
  });
});

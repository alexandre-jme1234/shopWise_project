import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { RendezVousService } from './rendez-vous-service';

describe('RendezVousService', () => {
  let service: RendezVousService;
  let http: HttpTestingController;          // faux serveur : on inspecte les requêtes envoyées

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(RendezVousService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());           // aucune requête inattendue

  it('lister sans filtre : GET sans paramètre', () => {
    service.lister(1, {}).subscribe(liste => expect(liste.length).toBe(0));
    const req = http.expectOne('/shops/1/rendez-vous');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.keys().length).toBe(0);
    req.flush([]);
  });

  it('lister avec filtres : envoie date, statut et client_id', () => {
    service.lister(1, { date: '2026-11-02', statut: 'HONORE', clientId: 4 }).subscribe();
    const req = http.expectOne(r => r.url === '/shops/1/rendez-vous');
    expect(req.request.params.get('date')).toBe('2026-11-02');
    expect(req.request.params.get('statut')).toBe('HONORE');
    expect(req.request.params.get('client_id')).toBe('4');
    req.flush([]);
  });

  it('honorer : PATCH sur /honorer', () => {
    service.honorer(1, 7).subscribe(rdv => expect(rdv.statut).toBe('HONORE'));
    const req = http.expectOne('/shops/1/rendez-vous/7/honorer');
    expect(req.request.method).toBe('PATCH');
    req.flush({ id: 7, client_id: 4, service_id: 1, statut: 'HONORE', date_heure: '2026-11-02T10:00:00' });
  });
});

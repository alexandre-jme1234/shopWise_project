import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ClientService } from './client-service';

describe('ClientService', () => {
  let service: ClientService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(ClientService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('listerParBoutique : GET /shops/{id}/clients', () => {
    service.listerParBoutique(2).subscribe(liste => expect(liste.length).toBe(1));
    const req = http.expectOne('/shops/2/clients');
    expect(req.request.method).toBe('GET');
    req.flush([{ id: 4, name: 'Marie', email: 'm@s.fr', phone: null, solde_points: 10 }]);
  });

  it('connecter : POST /auth/login avec email et mot de passe', () => {
    service.connecter('m@s.fr', 'secret123').subscribe(c => expect(c.id).toBe(4));
    const req = http.expectOne('/auth/login');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ email: 'm@s.fr', password: 'secret123' });
    req.flush({ id: 4, name: 'Marie', email: 'm@s.fr', phone: null, solde_points: 10 });
  });

  it('consulterPoints : GET /clients/{id}/points', () => {
    service.consulterPoints(4).subscribe(p => expect(p.solde_points).toBe(10));
    http.expectOne('/clients/4/points').flush({ client_id: 4, solde_points: 10, transactions: [] });
  });
});

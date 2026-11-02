import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RendezVous, Statut } from '../models/rendez-vous';

@Injectable({ providedIn: 'root' })          // ≈ @Service en Spring : un seul exemplaire, injectable partout
export class RendezVousService {

  constructor(private http: HttpClient) {}   // ≈ @Autowired

  lister(shopId: number, filtres: { date?: string; statut?: Statut | ''; clientId?: number | null }): Observable<RendezVous[]> {
    let params = new HttpParams();
    if (filtres.date)     { params = params.set('date', filtres.date); }
    if (filtres.statut)   { params = params.set('statut', filtres.statut); }
    if (filtres.clientId) { params = params.set('client_id', filtres.clientId); }   // nom du @RequestParam côté Java ?
    return this.http.get<RendezVous[]>(`/shops/${shopId}/rendez-vous`, { params });
  }

  honorer(shopId: number, rendezVousId: number): Observable<RendezVous> {
    return this.http.patch<RendezVous>(`/shops/${shopId}/rendez-vous/${rendezVousId}/honorer`, {});  // quel verbe HTTP ? quelle fin d'URL ?
  }
}
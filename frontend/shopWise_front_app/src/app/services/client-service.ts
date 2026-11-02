import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client, Points } from '../models/client';

@Injectable({ providedIn: 'root' })
export class ClientService {

  constructor(private http: HttpClient) {}

  listerParBoutique(shopId: number): Observable<Client[]> {
    return this.http.get<Client[]>(`/shops/${shopId}/clients`);
  }

  connecter(email: string, password: string): Observable<Client> {
    return this.http.post<Client>('/auth/login', { email, password });
  }

  consulterPoints(clientId: number): Observable<Points> {
    return this.http.get<Points>(`/clients/${clientId}/points`);
  }
}

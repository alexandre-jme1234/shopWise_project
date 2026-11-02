import { Component } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClientService } from '../services/client-service';
import { Client, Points } from '../models/client';

@Component({
  selector: 'app-espace-client',
  standalone: true,
  imports: [FormsModule, DatePipe],
  templateUrl: './espace-client.component.html'
})
export class EspaceClientComponent {
  email = '';
  password = '';
  client: Client | null = null;     // null = pas connecté
  points: Points | null = null;
  erreur = '';

  constructor(private clientService: ClientService) {}

  connecter(): void {
    this.erreur = '';
    this.clientService.connecter(this.email, this.password).subscribe({
      next: client => {
        this.client = client;
        this.password = '';          // on ne garde pas le mot de passe en mémoire
        this.chargerPoints(client.id);
      },
      error: () => this.erreur = 'Email ou mot de passe incorrect'
    });
  }

  chargerPoints(clientId: number): void {
    this.clientService.consulterPoints(clientId).subscribe({
      next: points => this.points = points,
      error: () => this.erreur = 'Impossible de charger vos points'
    });
  }

  deconnecter(): void {
    this.client = null;
    this.points = null;
  }
}

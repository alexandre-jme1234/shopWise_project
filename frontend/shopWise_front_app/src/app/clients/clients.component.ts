import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClientService } from '../services/client-service';
import { Client } from '../models/client';

@Component({
  selector: 'app-clients',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './clients.component.html'
})
export class ClientsComponent implements OnInit {
  shopId = 1;
  clients: Client[] = [];
  erreur = '';

  constructor(private clientService: ClientService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.erreur = '';
    this.clientService.listerParBoutique(this.shopId).subscribe({
      next: liste => this.clients = liste,
      error: () => { this.clients = []; this.erreur = `Boutique ${this.shopId} introuvable`; }
    });
  }
}

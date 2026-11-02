import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ClientService } from '../services/client-service';
import { Client, ClientForm } from '../models/client';

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
  message = '';
  enEdition: number | null = null;              // id du client modifié, null = création
  formulaire: ClientForm & { password: string } = { name: '', email: '', phone: '', password: '' };

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

  modifier(client: Client): void {
    this.enEdition = client.id;
    this.formulaire = { name: client.name, email: client.email, phone: client.phone ?? '', password: '' };
  }

  annulerEdition(): void {
    this.enEdition = null;
    this.formulaire = { name: '', email: '', phone: '', password: '' };
  }

  enregistrer(): void {
    this.erreur = '';
    const { password, ...infos } = this.formulaire;
    const requete = this.enEdition === null
      ? this.clientService.creer(this.shopId, this.formulaire)
      : this.clientService.modifier(this.shopId, this.enEdition, infos);
    requete.subscribe({
      next: client => { this.message = `Fiche de ${client.name} enregistrée`; this.annulerEdition(); this.charger(); },
      error: () => this.erreur = 'Enregistrement impossible (email déjà utilisé ou champs invalides)'
    });
  }
}

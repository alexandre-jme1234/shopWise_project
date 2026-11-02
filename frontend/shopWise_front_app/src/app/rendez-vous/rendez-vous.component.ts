import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RendezVousService } from '../services/rendez-vous-service';
import { RendezVous, Statut } from '../models/rendez-vous';

@Component({
  selector: 'app-rendez-vous',
  standalone: true,
  imports: [FormsModule, DatePipe],
  templateUrl: './rendez-vous.component.html'
})
export class RendezVousComponent implements OnInit {
  shopId = 1;
  filtres: { date: string; statut: Statut | ''; clientId: number | null } = { date: '', statut: '', clientId: null };
  rendezVous: RendezVous[] = [];
  erreur = '';

  constructor(private rendezVousService: RendezVousService) {}

  ngOnInit(): void {              // appelé une fois, à l'affichage de l'écran
    this.charger();
  }

  charger(): void {
    this.erreur = '';
    this.rendezVousService.lister(this.shopId, this.filtres).subscribe({
      next: liste => this.rendezVous = liste,                       // 200 : on affiche
      error: () => this.erreur = 'Impossible de charger les rendez-vous (boutique inconnue ?)'
    });
  }

  honorer(rdv: RendezVous): void {
    this.rendezVousService.honorer(this.shopId, rdv.id).subscribe({
      next: () => this.charger(),                                    // on recharge : le statut a changé
      error: () => this.erreur = `Le rendez-vous ${rdv.id} ne peut pas être honoré`
    });
  }
}

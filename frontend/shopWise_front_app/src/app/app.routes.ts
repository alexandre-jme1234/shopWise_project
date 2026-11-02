import { Routes } from '@angular/router';
import { RendezVousComponent } from './rendez-vous/rendez-vous.component';
import { ClientsComponent } from './clients/clients.component';
import { EspaceClientComponent } from './espace-client/espace-client.component';

export const routes: Routes = [
  { path: '', redirectTo: 'rendez-vous', pathMatch: 'full' },
  { path: 'rendez-vous', component: RendezVousComponent },
  { path: 'clients', component: ClientsComponent },
  { path: 'espace-client', component: EspaceClientComponent },
  { path: '**', redirectTo: 'rendez-vous' }
];

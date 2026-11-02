// Miroir du JSON renvoyé par le back (RendezVousResponse.java)
export type Statut = 'PLANIFIE' | 'HONORE' | 'ANNULE';

export interface RendezVous {
  id: number;
  client_id: number;
  service_id: number;
  statut: Statut;
  date_heure: string;
}

// Miroirs des DTO du back (ClientResponse, PointsResponse, TransactionPointsResponse)
export interface Client {
  id: number;
  name: string;
  email: string;
  phone: string | null;
  solde_points: number;
}

export interface TransactionPoints {
  id: number;
  type: 'GAIN' | 'UTILISATION';
  points: number;
  date_heure: string;
  rendez_vous_id: number | null;
}

export interface Points {
  client_id: number;
  solde_points: number;
  transactions: TransactionPoints[];
}

import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Session } from '../interfaces/session.interface';
import { jwtDecode } from 'jwt-decode';


@Injectable({
  providedIn: 'root'
})
export class SessionService {
  // Clé utilisée pour stocker la session dans le localStorage.
  private readonly SESSION_STORAGE_KEY = 'userSession';

  // Sujet pour gérer l'état de connexion de manière réactive.
  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLoggedIn());

  // Session courante, peut être indéfinie si non connecté.
  public session: Session | undefined;

  // Au moment de la création du service, charge la session depuis le localStorage si elle existe.
  constructor() {
    this.loadSession();
  }

  // Permet aux composants de s'abonner à l'état de connexion.
  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  // Connecte l'utilisateur  
  public logIn(user: Session): void {
    this.session = user;
    this.saveSession(); // Sauvegarde la session dans le localStorage.
    this.isLoggedSubject.next(true); // Met à jour l'état de connexion.
  }

  // Déconnecte l'utilisateur
  public logOut(): void {
    this.session = undefined;
    this.clearSession(); // Supprime la session du localStorage.
    this.isLoggedSubject.next(false); // Met à jour l'état de connexion.
  }

   // Retourne la session courante, peut être indéfinie.
  public getSession(): Session | undefined {
    return this.session;
  }

  // Récupère le token de la session depuis le localStorage, si la session existe.
  public getToken(): string | null {
    const session = localStorage.getItem(this.SESSION_STORAGE_KEY);
    if (session) {
      const parsedSession = JSON.parse(session);
      return parsedSession.token || null;
    }
    return null;
  }

    // Sauvegarde la session dans le localStorage.
  private saveSession(): void {
    if (this.session) {
      localStorage.setItem(this.SESSION_STORAGE_KEY, JSON.stringify(this.session));
    }
  }

  // Charge la session depuis le localStorage et vérifie sa validité.
  private loadSession(): void {
    const sessionData = localStorage.getItem(this.SESSION_STORAGE_KEY);
    if (sessionData) {
      try {
        const session = JSON.parse(sessionData) as Session;
        if (this.isValidSession(session)) {
          this.session = session;
          this.isLoggedSubject.next(true);
        } else {
          this.clearSession();
        }
      } catch (error) {
        console.error('Error parsing session data:', error);
        this.clearSession();
      }
    }
  }

  // Efface la session du localStorage et met à jour l'état de connexion.
  private clearSession(): void {
    localStorage.removeItem(this.SESSION_STORAGE_KEY);
    this.session = undefined;
    this.isLoggedSubject.next(false);
  }

  // Vérifie si l'utilisateur est connecté en s'assurant que le token n'est pas expiré.
  public isLoggedIn(): boolean {
    const token = this.getToken();
    return !!token && !this.isTokenExpired(token);
  }

   // Vérifie la validité de la session en s'assurant que tous les champs nécessaires sont présents et que le token n'est pas expiré.
  private isValidSession(session: Session): boolean {
    return !!(session.token && session.id && session.email && session.username) && !this.isTokenExpired(session.token);
  }

  // Vérifie si le token est expiré en décodant le JWT et en comparant la date d'expiration avec la date actuelle.
  private isTokenExpired(token: string): boolean {
    try {
      const decodedToken: any = jwtDecode(token);
      const expirationDate = new Date(0);
      expirationDate.setUTCSeconds(decodedToken.exp);

      return expirationDate < new Date();
    } catch (error) {
      console.error('Error decoding token:', error);
      return true;
    }
  }
}

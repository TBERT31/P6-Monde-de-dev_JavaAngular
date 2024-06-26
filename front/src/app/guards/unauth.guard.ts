import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router"; 
import { SessionService } from "../services/session.service";

@Injectable({providedIn: 'root'})
export class UnauthGuard implements CanActivate {

  // Injection du Router et du SessionService pour rediriger l'utilisateur et vérifier son statut de connexion.
  constructor( 
    private router: Router,
    private sessionService: SessionService,
  ) {}

  // Méthode canActivate appelée par le Router pour déterminer si une route peut être activée.
  canActivate(): boolean {
    let isLogged: boolean = false;
    // Souscription au statut de connexion pour déterminer si l'utilisateur est connecté.
    this.sessionService.$isLogged().subscribe(loggedIn => {
      isLogged = loggedIn;
    });
    // Si l'utilisateur est connecté, redirige vers la page des articles et empêche l'activation de la route ciblée par le garde.
    if (isLogged) {
      this.router.navigate(['articles']);
      return false;
    }
    // Si l'utilisateur n'est pas connecté, permet l'activation de la route.
    return true;
  }
}
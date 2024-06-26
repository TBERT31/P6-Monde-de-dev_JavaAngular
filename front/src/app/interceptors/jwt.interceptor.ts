import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SessionService } from '../services/session.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private sessionService: SessionService) { }

  // Méthode intercept qui est appelée pour chaque requête HTTP. Elle modifie la requête pour y ajouter le token JWT dans les headers.
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Récupération du token JWT depuis le service de session.
    const token = this.sessionService.getToken();

    // Si un token est présent, clone la requête HTTP pour y ajouter le header d'autorisation avec le token.
    if (token) {
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}` // Format d'autorisation Bearer utilisé pour les tokens JWT.
        }
      });

      // Passe la requête modifiée au prochain intercepteur dans la chaîne.
      return next.handle(cloned);
    }

    // Si aucun token n'est présent, passe la requête originale au prochain intercepteur dans la chaîne.
    return next.handle(req);
  }
}

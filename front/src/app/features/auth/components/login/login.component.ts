import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Session } from 'src/app/interfaces/session.interface';
import { SessionService } from 'src/app/services/session.service';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnDestroy {
  // Propriété pour gérer l'affichage des erreurs de connexion.
  public onError = false;
  // Sujet RxJS pour gérer la désinscription des observables à la destruction du composant.
  private destroy$ = new Subject<void>();

  // Expression régulière pour valider le format du mot de passe.
  private readonly passwordPattern = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=,?;./:!§£*()-_¨µ<>{}]).{8,}$/;

  // Déclaration et initialisation du formulaire avec ses contrôles et validations.
  public form = this.fb.group({
    emailOrUsername: [
      '',
      [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50)
      ]
    ],
    password: [
      '',
      [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(120),
        Validators.pattern(this.passwordPattern) 
      ]
    ]
  });

  // Constructeur pour injecter les services nécessaires.
  constructor(private authService: AuthService,
              private fb: FormBuilder,
              private router: Router,
              private sessionService: SessionService) {
  }

  // Méthode appelée lors de la soumission du formulaire.
  public submit(): void {
    // Création de l'objet de requête de connexion à partir des valeurs du formulaire.
    const loginRequest = this.form.value as LoginRequest;
    // Appel au service d'authentification pour se connecter.
    this.authService.login(loginRequest).pipe(
      takeUntil(this.destroy$) // Utilisation de takeUntil pour se désabonner automatiquement.
    ).subscribe({
      // En cas de succès, enregistrer la session et rediriger vers la page des articles.
      next: (response: Session) => {
        this.sessionService.logIn(response);
        this.router.navigate(['/articles']);
      },
      error: error => this.onError = true, // En cas d'erreur, activer l'affichage de l'erreur.
    });
  }

  // Méthode appelée à la destruction du composant pour nettoyer les abonnements.
  ngOnDestroy(): void {
    this.destroy$.next(); // Signaler la fin des abonnements.
    this.destroy$.complete(); // Terminer le sujet.
  }
}

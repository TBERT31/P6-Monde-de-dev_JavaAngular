import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnDestroy {
  // Propriété pour gérer l'affichage des erreurs de l'enregistrement.
  public onError = false;
  // Sujet RxJS pour gérer la désinscription des observables à la destruction du composant.
  private destroy$ = new Subject<void>();

  // Expression régulière pour valider le format du mot de passe.
  private readonly passwordPattern = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=,?;./:!§£*()-_¨µ<>{}]).{8,}$/;

  // Déclaration et initialisation du formulaire avec ses contrôles et validations.
  public form = this.fb.group({
    username: [
      '',
      [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50)
      ]
    ],
    email: [
      '',
      [
        Validators.required,
        Validators.email,
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
              private router: Router) {
  }

  // Méthode appelée lors de la soumission du formulaire.
  public submit(): void {
    // Création de l'objet de requête d'enregistrement à partir des valeurs du formulaire.
    const registerRequest = this.form.value as RegisterRequest;
    // Appel au service d'authentification pour s'enregistrer.
    this.authService.register(registerRequest).pipe(
      takeUntil(this.destroy$) // Utilisation de takeUntil pour se désabonner automatiquement.
    ).subscribe({
        next: (_: void) => this.router.navigate(['/login']), // En cas de succès, rediriger vers la page de connexion.
        error: _ => this.onError = true, // En cas d'erreur, activer l'affichage de l'erreur.
      }
    );
  }

  // Méthode appelée à la destruction du composant pour nettoyer les abonnements.
  ngOnDestroy(): void {
    this.destroy$.next(); // Signaler la fin des abonnements.
    this.destroy$.complete(); // Terminer le sujet.
  }
}

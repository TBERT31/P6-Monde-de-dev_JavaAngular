import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { User } from '../../interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { Session } from 'src/app/interfaces/session.interface';
import { Topic } from 'src/app/features/topics/interfaces/topic.interface';
import { TopicService } from 'src/app/features/topics/services/topic.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit, OnDestroy {
  // Déclaration des variables utilisées dans le composant
  public userForm: FormGroup;
  private user: User | undefined;
  private userId: number = 0;
  private username: string = "";
  private email: string = "";
  public errorMsg: string = "";
  public topicsSubscribed: Topic[] = [];
  private subscriptions: Subscription = new Subscription();

  // Constructeur du composant, injection des dépendances et initialisation du formulaire
  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private matSnackBar: MatSnackBar,
    private router: Router,
    private sessionService: SessionService,
    private topicService: TopicService
  ) {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.minLength(3), Validators.maxLength(50)]],
    });
  }

  // Méthode exécutée à l'initialisation du composant
  ngOnInit(): void {
    const session = this.sessionService.getSession(); // Récupération de la session
    if (session) {
      this.userId = session.id; // Récupération de l'id de l'utilisateur connecté
      this.username = session.username; // Récupération du nom d'utilisateur de l'utilisateur connecté
      this.email = session.email; // Récupération de l'email de l'utilisateur connecté
      const sub = this.userService.getUserById(session.id).subscribe(
        // Récupération de l'utilisateur connecté
        user => {
          this.user = user;

          //Déclanche une erreur si les informations fournies ne correspondent pas aux informations de session
          if (this.user === undefined || this.user.username !== this.username || this.user.email !== this.email) {
            throw new HttpErrorResponse({ status: 401, statusText: 'Unauthorized', error: { message: 'Session expired' } });
          }
          
          this.initForm(user);
          this.loadTopcisSubscribed();
        },
        // Redirection vers la page de connexion en cas d'erreur
        (error: HttpErrorResponse) => {
          this.router.navigate(['/login']);
        }
      );
      this.subscriptions.add(sub);
    } else {
       // Redirection vers la page de connexion si l'utilisateur n'est pas connecté
      this.router.navigate(['/login']);
    }
  }

  // Méthode exécutée à la destruction du composant
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe(); // Désabonnement de tous les observables
  }

  // Méthode d'initialisation du formulaire avec les données de l'utilisateur
  private initForm(user: User): void {
    this.userForm.patchValue({
      username: user.username,
      email: user.email,
    });
  }

  // Méthode de chargement des topics auxquels l'utilisateur est abonné
  private loadTopcisSubscribed(): void {
    const sub = this.topicService.getTopicsByUserId(this.userId).subscribe(topics => {
      this.topicsSubscribed = topics;
    });
    this.subscriptions.add(sub);
  }

  // Soumet le formulaire et met à jour les informations de l'utilisateur
  public submit(): void {
    if (this.userForm.valid) {
      // Mise à jour des informations de l'utilisateur
      const updatedUser: User = { ...this.user, ...this.userForm.value };
      const sub = this.userService.updateEmailOrUsername(updatedUser).subscribe({
        next: (response: Session) => {
          this.sessionService.logOut(); // Déconnexion de l'utilisateur
          this.sessionService.logIn(response); // Connexion de l'utilisateur avec les nouvelles informations
          this.matSnackBar.open('Profil mis à jour avec succès', 'Fermer', { duration: 3000 }); // Affichage d'un message de succès
        },
        error: error => {
          console.error(error.error.message);
          this.errorMsg = "Une erreur est survenue lors de la mise à jour du profil. Veuillez réessayer plus tard."; 
          this.matSnackBar.open('Erreur lors de la mise à jour du profil: '+error.error.message, 'Fermer', { duration: 3000 }) // Affichage d'un message d'erreur
        }
      });
      this.subscriptions.add(sub);
    } else {
      this.userForm.markAllAsTouched(); // Affiche les erreurs du formulaire
    }
  }

  // Déconnecte l'utilisateur
  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate(['']);
  }

  // Permet à l'utilisateur de se désabonner d'un topic
  public unsubscribeToTopic(topic: Topic): void {
    if (this.userId !== undefined) {
      const sub = this.topicService.unsubscribeUserToTopic(topic.id, this.userId).subscribe(() => {
        this.topicsSubscribed = this.topicsSubscribed.filter(t => t.id !== topic.id); // Suppression du topic de la liste des topics auxquels l'utilisateur est abonné
        this.matSnackBar.open('Vous êtes désabonné du topic : ' + topic.title, 'Fermer', { duration: 3000 }); // Affichage d'un message de succès
      });
      this.subscriptions.add(sub);
    }
  }
}

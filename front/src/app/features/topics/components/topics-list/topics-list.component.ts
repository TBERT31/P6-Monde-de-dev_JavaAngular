import { Component, OnInit, OnDestroy } from '@angular/core';
import { Topic } from '../../interfaces/topic.interface';
import { Observable, Subscription } from 'rxjs';
import { TopicService } from '../../services/topic.service';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-topics-list',
  templateUrl: './topics-list.component.html',
  styleUrls: ['./topics-list.component.scss']
})
export class TopicsListComponent implements OnInit, OnDestroy {
  // Déclaration d'une observable pour les sujets et d'une variable pour l'ID de l'utilisateur.
  public topics$: Observable<Topic[]>;
  private userId: number | undefined;
  // Gestion des abonnements pour éviter les fuites de mémoire.
  private subscriptions: Subscription = new Subscription();

  // Constructeur pour injecter les services nécessaires.
  constructor(
    private topicService: TopicService,
    private sessionService: SessionService
  ) {
    // Initialisation de l'observable des sujets avec la liste de tous les sujets.
    this.topics$ = this.topicService.getAllTopics();
  }

  // Méthode exécutée à l'initialisation du composant.
  ngOnInit(): void {
    // Récupération de la session pour obtenir l'ID de l'utilisateur connecté.
    const session = this.sessionService.getSession();
    if (session) {
      this.userId = session.id;
    }
  }

  // Méthode exécutée à la destruction du composant pour nettoyer les abonnements.
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  // Méthode pour vérifier si l'utilisateur est abonné à un sujet.
  public isSubscribed(topic: Topic): boolean {
    return topic.users_subscribed.includes(this.userId!);
  }

  // Méthode pour s'abonner à un sujet.
  public subscribeToTopic(topic: Topic): void {
    if (this.userId !== undefined) {
      // Appel au service pour s'abonner au sujet et mise à jour de la liste des utilisateurs abonnés.
      const sub = this.topicService.subscribeUserToTopic(topic.id, this.userId).subscribe(() => {
        topic.users_subscribed.push(this.userId!);
      });
      // Ajout de l'abonnement à la liste des abonnements pour gestion ultérieure.
      this.subscriptions.add(sub);
    }
  }

  // Méthode pour se désabonner d'un sujet.
  public unsubscribeToTopic(topic: Topic): void {
    if (this.userId !== undefined) {
      // Appel au service pour se désabonner du sujet et mise à jour de la liste des utilisateurs abonnés.
      const sub = this.topicService.unsubscribeUserToTopic(topic.id, this.userId).subscribe(() => {
        const index = topic.users_subscribed.indexOf(this.userId!);
        if (index > -1) {
          topic.users_subscribed.splice(index, 1);
        }
      });
      // Ajout de l'abonnement à la liste des abonnements pour gestion ultérieure.
      this.subscriptions.add(sub);
    }
  }
}

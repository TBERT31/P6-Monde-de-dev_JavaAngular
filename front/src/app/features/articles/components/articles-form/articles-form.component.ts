import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TopicService } from 'src/app/features/topics/services/topic.service';
import { Router } from '@angular/router';
import { Article } from '../../interfaces/article.interface';
import { ArticlesService } from '../../services/articles.service';
import { SessionService } from 'src/app/services/session.service';
import {  Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-article-form',
  templateUrl: './articles-form.component.html',
  styleUrls: ['./articles-form.component.scss']
})
export class ArticleFormComponent implements OnInit, OnDestroy {
  // Déclaration des propriétés du composant.
  public articleForm!: FormGroup;
  public topics$ =  this.topicService.getAllTopics();
  public username: string = "";
  private subscriptions: Subscription = new Subscription();

  // Constructeur du composant, injection des dépendances nécessaires.
  constructor(
    private fb: FormBuilder,
    private articlesService: ArticlesService,
    private topicService: TopicService,
    private matSnackBar: MatSnackBar,
    private router: Router,
    private sessionService: SessionService,
  ) {
    const session = this.sessionService.getSession(); // Récupération de la session courante.
    this.username = session!.username; // Attribution du nom d'utilisateur.
  }

  // Méthode appelée à l'initialisation du composant.
  ngOnInit(): void {
    this.initForm(); // Initialisation du formulaire.
    const initSub = this.topics$.subscribe(); // Souscription aux sujets disponibles.
    this.subscriptions.add(initSub); // Ajout de l'abonnement à la gestion des abonnements.
  }

  // Méthode appelée à la destruction du composant.
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe(); // Désabonnement de tous les observables pour éviter les fuites de mémoire.
  }

  // Initialisation du formulaire avec ou sans données d'article existant.
  public initForm(article?: Article): void {
      this.articleForm = this.fb.group({
        topic_title: [article ? article.topic_title : '', [Validators.required]],
        title: [article ? article.title : '', [Validators.required, Validators.maxLength(200)]],
        content: [article ? article.content : '', [Validators.required, Validators.maxLength(2000)]],
      });
  }

  // Soumission du formulaire pour créer ou mettre à jour un article.
  public submit(): void {
    if (this.articleForm!.invalid) {
      return; // Si le formulaire est invalide, ne rien faire.
    }

    const article = this.articleForm!.value as Article; // Récupération des données du formulaire.
    article.author = this.username; // Attribution de l'auteur de l'article.

    // Création ou mise à jour de l'article via le service ArticlesService.
    const articleSub = this.articlesService
      .createArticle(article)
      .subscribe({
        next: (newArticle: Article) => this.exitPage('Article créé!'),
        error: (err: HttpErrorResponse) => this.matSnackBar.open('Erreur durant la création de l\'article '+err.error.message, 'Close', { duration: 3000 })
    });
    this.subscriptions.add(articleSub); // Ajout de l'abonnement à la gestion des abonnements.
  }

  // Méthode pour quitter la page actuelle après la création ou la mise à jour d'un article.
  public exitPage(message: string): void {
    this.matSnackBar.open(message, 'Close', { duration: 3000 }); 
    this.router.navigate(['articles']); 
  }

  // Méthode pour revenir à la page précédente.
  public back(): void {
    window.history.back();
  }
}

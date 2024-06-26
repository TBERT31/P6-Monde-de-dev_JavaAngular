import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { Article } from '../../interfaces/article.interface';
import { SessionService } from 'src/app/services/session.service';
import { CommentRequest } from '../../interfaces/api/commentRequest.interface';
import { CommentsService } from '../../services/comments.service';
import { Comment } from '../../interfaces/comment.interface';
import { ArticlesService } from '../../services/articles.service';
import { Observable, Subscription } from 'rxjs';


@Component({
  selector: 'app-article-detail',
  templateUrl: './articles-detail.component.html',
  styleUrls: ['./articles-detail.component.scss']
})
export class ArticleDetailComponent implements OnInit, OnDestroy {
  // Déclaration des propriétés du composant.
  public commentForm!: FormGroup;
  public article: Article | undefined;
  private username: string | undefined;
  public comments$: Observable<Comment[]> | undefined;
  private subscriptions: Subscription = new Subscription();

  // Constructeur du composant, injection des dépendances nécessaires.
  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private commentsService: CommentsService,
    private articlesService: ArticlesService,
    private sessionService: SessionService,
    private matSnackBar: MatSnackBar,
  ) { 
    this.initCommentForm(); // Initialisation du formulaire de commentaire.
  }

  // Méthode appelée à l'initialisation du composant.
  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!; // Récupération de l'ID de l'article depuis l'URL.

    const session = this.sessionService.getSession(); // Récupération de la session courante.
    if (session) {
      this.username = session.username; // Mise à jour du nom d'utilisateur.
    }

    // Souscription au service pour récupérer l'article par son ID et mise à jour de l'article courant.
    const articleSubscription = this.articlesService
      .getArticleById(id)
      .subscribe((article: Article) => {
        this.article = article;
        this.loadComments(); // Chargement des commentaires pour l'article courant.
    });

    this.subscriptions.add(articleSubscription); // Ajout de l'abonnement à la gestion des abonnements.
  }

  // Méthode appelée à la destruction du composant.
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe(); // Désabonnement de tous les observables pour éviter les fuites de mémoire.
  }

  // Initialisation du formulaire de commentaire avec validation.
  private initCommentForm(): void{
    this.commentForm = this.fb.group({
      message: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(2000)]],
    });
  }

  // Chargement des commentaires pour l'article courant. 
  // (Pas besoin d'unsubscribe cet observable là le pipe async s'en charge)
  private loadComments(): void{
    if (this.article) {
      this.comments$ = this.commentsService.getCommentByArticleId(this.article.id);
    }
  }

  // Envoi d'un commentaire.
  public sendComment(): void {
    if (this.article && this.username) {
      const comment = {
        article_id: this.article.id,
        username: this.username,
        message: this.commentForm.value.message
      } as CommentRequest;
 
      // Création du commentaire via le service et réinitialisation du formulaire et rechargement des commentaires après succès.
      const commentSubscription = this.commentsService.createComment(comment).subscribe(
        (commentResponse: Comment) => {
          this.initCommentForm();
          this.matSnackBar.open("Votre commentaire a été envoyé!", "Close", { duration: 3000 });
          this.loadComments(); 
        }
      );
      
      this.subscriptions.add(commentSubscription);
    }
  }

  // Méthode pour revenir à la page précédente.
  public back(): void {
    window.history.back();
  }
}

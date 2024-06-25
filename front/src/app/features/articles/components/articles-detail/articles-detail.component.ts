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

  public commentForm!: FormGroup;
  public article: Article | undefined;
  private username: string | undefined;
  public comments$: Observable<Comment[]> | undefined;
  private subscriptions: Subscription = new Subscription();

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private commentsService: CommentsService,
    private articlesService: ArticlesService,
    private sessionService: SessionService,
    private matSnackBar: MatSnackBar,
  ) { 
    this.initCommentForm();
  }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;

    const session = this.sessionService.getSession();
    if (session) {
      this.username = session.username;
    }

    const articleSubscription = this.articlesService
      .getArticleById(id)
      .subscribe((article: Article) => {
        this.article = article;
        this.loadComments();
    });

    this.subscriptions.add(articleSubscription);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private initCommentForm(): void{
    this.commentForm = this.fb.group({
      message: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(2000)]],
    });
  }

  // Pas besoin d'unsubscribe cet observable là le pipe async s'en charge
  private loadComments(): void{
    if (this.article) {
      this.comments$ = this.commentsService.getCommentByArticleId(this.article.id);
    }
  }

  public sendComment(): void {
    if (this.article && this.username) {
      const comment = {
        article_id: this.article.id,
        username: this.username,
        message: this.commentForm.value.message
      } as CommentRequest;

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

  public back(): void {
    window.history.back();
  }
}

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

  public articleForm!: FormGroup;
  public topics$ =  this.topicService.getAllTopics();
  public username: string = "";
  private subscriptions: Subscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private articlesService: ArticlesService,
    private topicService: TopicService,
    private matSnackBar: MatSnackBar,
    private router: Router,
    private sessionService: SessionService,
  ) {
    const session = this.sessionService.getSession();
    this.username = session!.username;
  }

  ngOnInit(): void {
    this.initForm();
    const initSub = this.topics$.subscribe();
    this.subscriptions.add(initSub);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  public initForm(article?: Article): void {
      this.articleForm = this.fb.group({
        topic_title: [article ? article.topic_title : '', [Validators.required]],
        title: [article ? article.title : '', [Validators.required, Validators.maxLength(200)]],
        content: [article ? article.content : '', [Validators.required, Validators.maxLength(2000)]],
      });
  }

  public submit(): void {
    if (this.articleForm!.invalid) {
      return;
    }

    const article = this.articleForm!.value as Article;
    article.author = this.username;

    const articleSub = this.articlesService
      .createArticle(article)
      .subscribe({
        next: (newArticle: Article) => this.exitPage('Article créé!'),
        error: (err: HttpErrorResponse) => this.matSnackBar.open('Erreur durant la création de l\'article '+err.error.message, 'Close', { duration: 3000 })
    });
    this.subscriptions.add(articleSub);
  }

  public exitPage(message: string): void {
    this.matSnackBar.open(message, 'Close', { duration: 3000 });
    this.router.navigate(['articles']);
  }

  public back() {
    window.history.back();
  }
}

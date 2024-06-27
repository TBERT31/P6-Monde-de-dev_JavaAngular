import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { Article } from '../../interfaces/article.interface';
import { ArticlesService } from '../../services/articles.service';

@Component({
  selector: 'app-articles-list',
  templateUrl: './articles-list.component.html',
  styleUrls: ['./articles-list.component.scss']
})
export class ArticlesListComponent implements OnInit, OnDestroy {
  public articles$: Observable<Article[]> | undefined;
  private sortBy: string = 'updatedAt';
  public order: string = 'desc';
  //private subscription: Subscription = new Subscription();

  constructor(private articleService: ArticlesService) {}

  // Initialisation du composant.
  ngOnInit(): void {
    this.loadArticles(); // Chargement des articles.
  }


  // Pas forcement utile car le pipe async s'occupe de désinscrire les observables
  ngOnDestroy():void {
    //this.subscription.unsubscribe();
  }

  // Chargement des articles en fonction du tri et de l'ordre.
  private loadArticles(): void {
    this.articles$ = this.articleService.getAllArticles(this.sortBy, this.order);
  }

  // Changement de l'ordre de tri.
  public toggleSortOrder(): void {
    this.order = this.order === 'asc' ? 'desc' : 'asc';
    this.loadArticles();
  }
}

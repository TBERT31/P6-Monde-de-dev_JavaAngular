import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Article } from '../../interfaces/article.interface';
import { ArticlesService } from '../../services/articles.service';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-articles-list',
  templateUrl: './articles-list.component.html',
  styleUrls: ['./articles-list.component.scss']
})
export class ArticlesListComponent implements OnInit {

  public articles$: Observable<Article[]> = this.articleService.getAllArticles();

  constructor(
    private articleService : ArticlesService,
  ) { }

  ngOnInit(): void {
  }

  

}

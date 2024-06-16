import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Article } from '../interfaces/article.interface'; 



@Injectable({
  providedIn: 'root'
})
export class ArticlesService {

  private pathService = 'api/articles';

  constructor(private httpClient: HttpClient) { }

  public getAllArticles(sortBy: string = 'id', order: string = 'asc'): Observable<Article[]> {
    let params = new HttpParams();
    if (sortBy) {
      params = params.set('sortBy', sortBy);
    }
    if (order) {
      params = params.set('order', order);
    }
    return this.httpClient.get<Article[]>(this.pathService, { params });
  }

  public getArticleById(id: number): Observable<Article> {
    return this.httpClient.get<Article>(`${this.pathService}/${id}`);
  }

  public getArticleByTopicId(topicId: number): Observable<Article[]> {
    return this.httpClient.get<Article[]>(`${this.pathService}/topic/${topicId}`);
  }

  public createArticle(form: FormData): Observable<Article> {
    return this.httpClient.post<Article>(this.pathService, form);
  }

}

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
    // Initialisation d'un objet HttpParams vide.
    let params = new HttpParams();

    // Si un critère de tri (sortBy) est fourni, il est ajouté à l'objet params.
    if (sortBy) {
      params = params.set('sortBy', sortBy);
    }

    // Si un ordre de tri (order) est fourni, il est également ajouté à l'objet params.
    if (order) {
      params = params.set('order', order);
    }

    // Les paramètres de tri (params) sont passés dans l'objet d'options de la requête.
    return this.httpClient.get<Article[]>(this.pathService, { params });
  }

  public getArticleById(id: number): Observable<Article> {
    return this.httpClient.get<Article>(`${this.pathService}/${id}`);
  }

  public getArticleByTopicId(topicId: number): Observable<Article[]> {
    return this.httpClient.get<Article[]>(`${this.pathService}/topic/${topicId}`);
  }

  public createArticle(article: Article): Observable<Article> {
    return this.httpClient.post<Article>(this.pathService, article);
  }

}

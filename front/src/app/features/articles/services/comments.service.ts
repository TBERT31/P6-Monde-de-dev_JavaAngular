import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Comment } from '../interfaces/comment.interface';
import { CommentRequest } from '../interfaces/api/commentRequest.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  private pathService = 'api/comments';

  constructor(private httpClient: HttpClient) { }

  public createComment(commentRequest: CommentRequest): Observable<Comment> {
    return this.httpClient.post<Comment>(this.pathService, commentRequest);
  } 

  public getCommentById(id: number): Observable<Comment> {
    return this.httpClient.get<Comment>(`${this.pathService}/${id}`);
  }

  public getCommentByArticleId(articleId: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(`${this.pathService}/article/${articleId}`);
  }
}
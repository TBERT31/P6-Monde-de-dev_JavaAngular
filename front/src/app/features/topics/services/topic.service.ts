import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../interfaces/topic.interface';

@Injectable({
  providedIn: 'root'
})
export class TeacherService {

    private pathService = 'api/topics';

    constructor(private httpClient: HttpClient) { }

    public getAllTopics(): Observable<Topic[]> {
        return this.httpClient.get<Topic[]>(this.pathService);
    }

    public getTopicById(id: number): Observable<Topic> {
        return this.httpClient.get<Topic>(`${this.pathService}/${id}`);
    }

    public subscribeUserToTopic(id: number, userId: number): Observable<Topic> {
        return this.httpClient.post<Topic>(`${this.pathService}/${id}/subscribe/${userId}`, null);
    }

    public unsubscribeUserToTopic(id: number, userId: number): Observable<Topic> {
        return this.httpClient.delete<Topic>(`${this.pathService}/${id}/subscribe/${userId}`);
    }
}

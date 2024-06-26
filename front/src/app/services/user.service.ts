import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';
import { Session } from '../interfaces/session.interface';
import { Topic } from '../features/topics/interfaces/topic.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {
    private pathService = 'api/users';

    constructor(private httpClient: HttpClient) { }

    public getUserById(id: number): Observable<User> {
        return this.httpClient.get<User>(`${this.pathService}/${id}`);
    }

    public getUserByEmail(email: string): Observable<User> {
        return this.httpClient.get<User>(`${this.pathService}/${email}`);
    }

    public updateUser(user: User): Observable<Session> {
        return this.httpClient.put<Session>(`${this.pathService}/${user.id}`, user);
    }
}

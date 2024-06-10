import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {
    private pathService = 'api/users';

    constructor(private httpClient: HttpClient) { }

    public getById(id: number): Observable<User> {
        return this.httpClient.get<User>(`${this.pathService}/${id}`);
    }

    public getByEmail(email: string): Observable<User> {
        return this.httpClient.get<User>(`${this.pathService}/${email}`);
    }
}

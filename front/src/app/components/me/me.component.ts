import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { User } from '../../interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { Observable, Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { Session } from 'src/app/interfaces/session.interface';
import { Topic } from 'src/app/features/topics/interfaces/topic.interface';
import { TopicService } from 'src/app/features/topics/services/topic.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss']
})
export class MeComponent implements OnInit, OnDestroy {
  public userForm: FormGroup;
  public user: User | undefined;
  public userId: number = 0;
  public username: string = "";
  public email: string = "";
  public errorMsg: string = "";
  public topicsSubscribed: Topic[] = [];
  private subscriptions: Subscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private matSnackBar: MatSnackBar,
    private router: Router,
    private sessionService: SessionService,
    private topicService: TopicService
  ) {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email, Validators.minLength(3), Validators.maxLength(50)]],
    });
  }

  ngOnInit(): void {
    const session = this.sessionService.getSession();
    if (session) {
      this.userId = session.id;
      this.username = session.username;
      this.email = session.email;
      const sub = this.userService.getUserById(session.id).subscribe(
        user => {
          this.user = user;
          this.initForm(user);
          this.loadTopcisSubscribed();
        },
        (error: HttpErrorResponse) => {
          this.router.navigate(['/login']);
        }
      );
      this.subscriptions.add(sub);
    } else {
      this.router.navigate(['/login']);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  public initForm(user: User): void {
    this.userForm.patchValue({
      username: user.username,
      email: user.email,
    });
  }

  loadTopcisSubscribed(): void {
    const sub = this.topicService.getTopicsByUserId(this.userId).subscribe(topics => {
      this.topicsSubscribed = topics;
    });
    this.subscriptions.add(sub);
  }

  public submit(): void {
    if (this.userForm.valid) {
      const updatedUser: User = { ...this.user, ...this.userForm.value };
      const sub = this.userService.updateUser(updatedUser).subscribe({
        next: (response: Session) => {
          this.sessionService.logOut();
          this.sessionService.logIn(response);
          this.matSnackBar.open('Profil mis à jour avec succès', 'Fermer', { duration: 3000 });
        },
        error: error => {
          console.error(error.error.message);
          this.errorMsg = "Une erreur est survenue lors de la mise à jour du profil. Veuillez réessayer plus tard.";
          this.matSnackBar.open('Erreur lors de la mise à jour du profil: '+error.error.message, 'Fermer', { duration: 3000 })
        }
      });
      this.subscriptions.add(sub);
    } else {
      this.userForm.markAllAsTouched();
    }
  }

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate(['']);
  }

  public unsubscribeToTopic(topic: Topic): void {
    if (this.userId !== undefined) {
      const sub = this.topicService.unsubscribeUserToTopic(topic.id, this.userId).subscribe(() => {
        this.topicsSubscribed = this.topicsSubscribed.filter(t => t.id !== topic.id);
        this.matSnackBar.open('Vous êtes désabonné du topic : ' + topic.title, 'Fermer', { duration: 3000 });
      });
      this.subscriptions.add(sub);
    }
  }
}

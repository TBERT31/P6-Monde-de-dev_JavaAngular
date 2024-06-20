import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/features/auth/services/auth.service'; 
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { User } from '../../interfaces/user.interface';
import { SessionService } from 'src/app/services/session.service';
import { Subscription } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { Session } from 'src/app/interfaces/session.interface';
import { LoginRequest } from 'src/app/features/auth/interfaces/loginRequest.interface';

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
  private subscriptions: Subscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private matSnackBar: MatSnackBar,
    private router: Router,
    private sessionService: SessionService,
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
      this.userService.getUserById(session.id).subscribe(
        user => {
          this.user = user;
          this.initForm(user);
        },
        (error: HttpErrorResponse) => {
          this.router.navigate(['/login']);
        }
      );
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

  public submit(): void {
    if (this.userForm.valid) {
      const updatedUser: User = { ...this.user, ...this.userForm.value };
      this.userService.updateUser(updatedUser).subscribe(
        response => {

          this.sessionService.logOut();

          this.userService.getUserById(this.userId).subscribe(
            user => {
              this.user = user;
              const updatedSession: Session = {
                token: this.sessionService.getToken() || '',
                type: 'Bearer', 
                id: user.id,
                email: user.email,
                username: user.username
              };
      
              this.sessionService.logIn(updatedSession);
            },
            (error: HttpErrorResponse) => {
              this.router.navigate(['/login']);
            }
          );

          this.matSnackBar.open('Profil mis à jour avec succès', 'Fermer', { duration: 3000 });
        },
        (error: HttpErrorResponse) => {
          this.matSnackBar.open('Erreur lors de la mise à jour du profil', 'Fermer', { duration: 3000 });
        }
      );
    } else {
      this.userForm.markAllAsTouched();
    }
  }

  public back(): void {
    window.history.back();
  }
}

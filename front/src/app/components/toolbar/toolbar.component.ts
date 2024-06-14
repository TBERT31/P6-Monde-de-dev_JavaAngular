import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { Router, NavigationEnd, Event } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { MatSidenav } from '@angular/material/sidenav';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent implements OnInit, OnDestroy {
  showToolbar: boolean = true;
  showToolbarSubscription: Subscription | null = null;
  isLoggedIn$: Observable<boolean>;
  @ViewChild('drawer') drawer!: MatSidenav;

  constructor(
    private authService: AuthService,
    private router: Router,
    private sessionService: SessionService
  ) {
    this.isLoggedIn$ = this.sessionService.$isLogged();
  }

  ngOnInit() {
    this.showToolbarSubscription = this.router.events.pipe(
      filter((event: Event): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.toolbarDisplayer(event);
    });
  }

  ngOnDestroy() {
    if (this.showToolbarSubscription) {
      this.showToolbarSubscription.unsubscribe();
    }
  }

  toolbarDisplayer(event: NavigationEnd) {
    this.showToolbar = !(event.url === '/' || event.url === '');
  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

  public logout(): void {
    this.sessionService.logOut();
    this.router.navigate(['']);
  }

  navigateHome(): void {
    this.isLoggedIn$.subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.router.navigate(['articles']);
      } else {
        this.router.navigate(['/']);
      }
    });
  }
}

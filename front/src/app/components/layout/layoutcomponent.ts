import { Component, OnInit, OnDestroy, ViewChild, HostListener } from '@angular/core';
import { Router, NavigationEnd, Event } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { MatSidenav } from '@angular/material/sidenav';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit, OnDestroy {
  @ViewChild('drawer') drawer!: MatSidenav;

  showToolbar: boolean = true;
  showToolbarSubscription: Subscription | null = null;
  resizeSubscription: Subscription | null = null;
  isLoggedIn$: Observable<boolean>;
  private readonly mobileWidthLoginsPages = 768;
  drawerCanBeVisible: boolean = window.innerWidth < 640;


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

    this.checkScreenWidth(window.innerWidth);
  }

  ngOnDestroy() {
    if (this.showToolbarSubscription) {
      this.showToolbarSubscription.unsubscribe();
    }
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: UIEvent) {
    const target = event.target as Window;
    this.checkScreenWidth(target.innerWidth);
  }

  checkScreenWidth(width: number) {
    const isMobile = width < this.mobileWidthLoginsPages;
    const isLoginOrRegister = this.router.url === '/login' || this.router.url === '/register';
    this.drawerCanBeVisible = width < 640;

    if (isMobile && isLoginOrRegister) {
      this.showToolbar = false;
    } else {
      this.showToolbar = true;
    }
  }


  toolbarDisplayer(event: NavigationEnd) {
    const isHome = event.url === '/';
    const isLoginOrRegister = event.url === '/login' || event.url === '/register';
    const isMobile = window.innerWidth < this.mobileWidthLoginsPages;

    if ((isMobile && isLoginOrRegister) || isHome) {
      this.showToolbar = false;
    } else {
      this.showToolbar = true;
    }
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

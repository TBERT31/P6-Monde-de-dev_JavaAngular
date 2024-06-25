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
    private router: Router,
    private sessionService: SessionService
  ) {
    this.isLoggedIn$ = this.sessionService.$isLogged();
  }

  ngOnInit() {
    this.showToolbarSubscription = this.router.events.pipe(
      filter((event: Event): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.updateToolbarVisibility(event.url);
    });

    this.updateToolbarVisibility(this.router.url, window.innerWidth);
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

  private checkScreenWidth(width: number) {
    this.drawerCanBeVisible = width < 640;
    this.updateToolbarVisibility(this.router.url, width);
  }

  private updateToolbarVisibility(url: string, width?: number) {
    const isHome = url === '/';
    const isLoginOrRegister = url === '/login' || url === '/register';
    const isMobile = (width ?? window.innerWidth) < this.mobileWidthLoginsPages;

    this.showToolbar = !(isMobile && isLoginOrRegister) && !isHome;
  }

  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
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

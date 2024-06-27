import { Component, OnInit, OnDestroy, ViewChild, HostListener } from '@angular/core';
import { Router, NavigationEnd, Event } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { SessionService } from 'src/app/services/session.service';
import { MatSidenav } from '@angular/material/sidenav';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit, OnDestroy {
  // Référence au drawer (sidenav) pour pouvoir le contrôler.
  @ViewChild('drawer') drawer!: MatSidenav;

  // Variables pour contrôler l'affichage de la barre d'outils et la visibilité du tiroir.
  public showToolbar: boolean = true;
  private isLoggedIn$: Observable<boolean>;
  private readonly mobileWidthLoginsPages = 768;
  public drawerCanBeVisible: boolean = window.innerWidth < 640;
  private subscriptions: Subscription = new Subscription();

  // Injection du Router et du SessionService dans le constructeur.
  constructor(
    private router: Router,
    private sessionService: SessionService
  ) {
    // Souscription au statut de connexion.
    this.isLoggedIn$ = this.sessionService.$isLogged();
  }

  // Initialisation du composant.
  ngOnInit() {
    // Souscription aux événements de navigation pour mettre à jour la visibilité de la barre d'outils.
    const showToolbarSub = this.router.events.pipe(
      filter((event: Event): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.updateToolbarVisibility(event.url);
    });

    // Ajout de la souscription à la collection pour une gestion centralisée.
    this.subscriptions.add(showToolbarSub);

    // Mise à jour initiale de la visibilité de la barre d'outils.
    this.updateToolbarVisibility(this.router.url, window.innerWidth);
  }

  // Nettoyage lors de la destruction du composant.
  ngOnDestroy() {
    // Désinscription de toutes les souscriptions pour éviter les fuites de mémoire.
    this.subscriptions.unsubscribe();
  }

  // Gestionnaire d'événements pour les changements de taille de fenêtre.  
  @HostListener('window:resize', ['$event'])
  onResize(event: UIEvent) {
    const target = event.target as Window;
    this.checkScreenWidth(target.innerWidth);
  }

  // Vérification de la largeur de l'écran pour ajuster la visibilité du drawer.
  private checkScreenWidth(width: number) {
    this.drawerCanBeVisible = width < 640;
    this.updateToolbarVisibility(this.router.url, width);
  }

  // Mise à jour de la visibilité de la barre d'outils en fonction de l'URL et de la largeur de l'écran.
  private updateToolbarVisibility(url: string, width?: number) {
    const isHome = url === '/';
    const isLoginOrRegister = url === '/login' || url === '/register';
    const isMobile = (width ?? window.innerWidth) < this.mobileWidthLoginsPages;

    this.showToolbar = !(isMobile && isLoginOrRegister) && !isHome;
  }

  // Fonction pour récupérer le statut de connexion.
  public $isLogged(): Observable<boolean> {
    return this.sessionService.$isLogged();
  }

  // Fonction pour naviguer vers la page d'accueil ou la page des articles en fonction du statut de connexion.
  public navigateHome(): void {
    const logInSub = this.isLoggedIn$.subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.router.navigate(['articles']);
      } else {
        this.router.navigate(['/']);
      }
    });

    // Ajout de la souscription à la collection pour une gestion centralisée.
    this.subscriptions.add(logInSub);
  }
}

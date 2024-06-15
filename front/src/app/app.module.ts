import { NgModule } from '@angular/core';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './interceptors/jwt.interceptor';
import { MatCardModule } from '@angular/material/card';
import { MeComponent } from './components/me/me.component';
import { LayoutComponent } from './components/layout/layoutcomponent';
import { HomeComponent } from './components/home/home.component';
import { TruncatePipe } from './pipes/truncate.pipe';


const materialModule = [
  MatButtonModule,
  MatCardModule,
  MatIconModule,
  MatSnackBarModule,
  MatToolbarModule,
  MatSidenavModule,
  MatListModule
]

@NgModule({
  declarations: [
    AppComponent,
    NotFoundComponent,
    MeComponent,
    LayoutComponent,
    HomeComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule, 
    HttpClientModule,
    ...materialModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}

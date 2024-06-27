import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TruncatePipe } from './pipes/truncate.pipe';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule } from '@angular/forms';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [
    TruncatePipe,
    NotFoundComponent,
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MaterialModule,
  ],
  exports: [
    TruncatePipe,
    ReactiveFormsModule,
    NotFoundComponent,
    MaterialModule, /* Il suffira d'importer SharedModule pour avoir accès 
    aux imports material nécessaire, WebPack ne multipliera pas les imports, 
    de notre côté cela nous allège le code en évitant de la redondance d'import */
  ]
})
export class SharedModule { }

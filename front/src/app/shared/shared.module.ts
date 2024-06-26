import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TruncatePipe } from './pipes/truncate.pipe';
import { MaterialModule } from './material.module';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    TruncatePipe
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MaterialModule,
  ],
  exports: [
    TruncatePipe,
    ReactiveFormsModule,
    MaterialModule, /* Il suffira d'importer SharedModule pour avoir accès 
    aux imports material nécessaire, WebPack ne multipliera pas les imports, 
    de notre côté cela nous allège le code en évitant de la redondance d'import */
  ]
})
export class SharedModule { }

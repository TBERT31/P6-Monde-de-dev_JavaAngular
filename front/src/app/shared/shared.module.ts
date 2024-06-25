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
    MaterialModule,
  ]
})
export class SharedModule { }

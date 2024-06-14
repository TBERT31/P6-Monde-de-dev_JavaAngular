import { NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { TopicsListComponent } from './components/topics-list/topics-list.component';
import { TopicsRoutingModule } from './topics-routing.module';
import localeFr from '@angular/common/locales/fr';
registerLocaleData(localeFr);

const materialModules = [
  MatButtonModule,
  MatCardModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatSnackBarModule,
  MatSelectModule
];

@NgModule({
  declarations: [
    TopicsListComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TopicsRoutingModule,
    ...materialModules
  ]
})
export class TopicsModule { }

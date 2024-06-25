import { NgModule } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { TopicsListComponent } from './components/topics-list/topics-list.component';
import { TopicsRoutingModule } from './topics-routing.module';
import localeFr from '@angular/common/locales/fr';
import { SharedModule } from '../../shared/shared.module';
registerLocaleData(localeFr);


@NgModule({
  declarations: [
    TopicsListComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TopicsRoutingModule,
    SharedModule,
  ]
})
export class TopicsModule { }

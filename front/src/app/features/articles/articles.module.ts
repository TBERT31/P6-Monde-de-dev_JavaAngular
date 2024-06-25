import { LOCALE_ID, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArticlesRoutingModule } from './articles-routing.module';
import { ArticlesListComponent } from './components/articles-list/articles-list.component';
import { ArticleFormComponent } from './components/articles-form/articles-form.component';
import { ArticleDetailComponent } from './components/articles-detail/articles-detail.component';
import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';
registerLocaleData(localeFr);



@NgModule({
  declarations: [
    ArticlesListComponent,
    ArticleFormComponent,
    ArticleDetailComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ArticlesRoutingModule,
    SharedModule,
  ],
  providers: [
    {
      provide: LOCALE_ID,
      useValue: 'fr-FR'
    },
  ]
})
export class ArticlesModule { }

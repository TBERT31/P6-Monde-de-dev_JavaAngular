import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArticlesListComponent } from './components/articles-list/articles-list.component';
import { ArticleFormComponent } from './components/articles-form/articles-form.component';
import { ArticleDetailComponent } from './components/articles-detail/articles-detail.component';



@NgModule({
  declarations: [
    ArticlesListComponent,
    ArticleFormComponent,
    ArticleDetailComponent
  ],
  imports: [
    CommonModule
  ]
})
export class ArticlesModule { }

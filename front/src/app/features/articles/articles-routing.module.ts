import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArticlesListComponent } from './components/articles-list/articles-list.component';
import { ArticleDetailComponent } from './components/articles-detail/articles-detail.component';
import { ArticleFormComponent } from './components/articles-form/articles-form.component';

const routes: Routes = [
    {title: "Articles", path: '', component: ArticlesListComponent},
    {title: "Articles - detail", path: 'detail/:id', component: ArticleDetailComponent},
    {title: "Articles - create", path: 'create', component: ArticleFormComponent},
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ArticlesRoutingModule { }
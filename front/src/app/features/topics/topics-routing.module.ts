import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TopicsListComponent } from './components/topics-list/topics-list.component';

const routes: Routes = [
    {title: "Topcis", path: '', component: TopicsListComponent},
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
    export class TopicsRoutingModule {
}
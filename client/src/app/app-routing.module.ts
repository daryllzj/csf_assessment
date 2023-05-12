import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { View1Component } from './component/view1/view1.component';
import { View2Component } from './component/view2/view2.component';
import { View0Component } from './component/view0/view0.component';

const routes: Routes = [
  {path:"", component: View0Component},
  {path: "view1", component: View1Component},
  {path: "view2/:bundleId", component: View2Component},
  {path: "**", redirectTo: "/", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

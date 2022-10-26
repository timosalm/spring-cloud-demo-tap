import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {AuthRouteGuard} from "./auth.route-guard";
import {OrderComponent} from "./order/order.component";

const routes: Routes = [
  { path: '**', component: HomeComponent},
  { path: 'orders', component: OrderComponent, canActivate: [AuthRouteGuard] }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

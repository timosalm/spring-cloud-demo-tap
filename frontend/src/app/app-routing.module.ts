import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {AuthRouteGuard} from "./auth/auth.route-guard";
import {OrderComponent} from "./order/order.component";

const routes: Routes = [
  { path: 'orders', component: OrderComponent, canActivate: [AuthRouteGuard] },
  { path: '**', component: HomeComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

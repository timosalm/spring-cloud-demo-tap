import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { OAuthModule } from "angular-oauth2-oidc";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {APP_BASE_HREF} from "@angular/common";
import {environment} from "../environments/environment";
import {HomeComponent} from "./home/home.component";
import {ClarityModule} from "@clr/angular";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ReactiveFormsModule} from "@angular/forms";
import {AuthHttpInterceptor} from "./auth/auth.http.interceptor";
import {OrderComponent} from "./order/order.component";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    OrderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: [environment.endpoints.orders, environment.endpoints.products],
        sendAccessToken: true
      }
    }),
    HttpClientModule,
    ClarityModule,
    BrowserAnimationsModule,
    ReactiveFormsModule
  ],
  providers: [
    {provide: APP_BASE_HREF, useValue: environment.baseHref},
    {provide: HTTP_INTERCEPTORS, useClass: AuthHttpInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

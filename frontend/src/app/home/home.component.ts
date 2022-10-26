import { Component } from '@angular/core';
import {OAuthService} from "angular-oauth2-oidc";
import {authCodeFlowConfig} from "../auth.config";

@Component({
  selector: 'home',
  templateUrl: './home.component.html'
})
export class HomeComponent {

  constructor(private oauthService: OAuthService) {}

  login() {
    this.oauthService.tryLoginCodeFlow();
  }
}

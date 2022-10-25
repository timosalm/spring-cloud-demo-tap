import { Component } from '@angular/core';
import { authCodeFlowConfig } from './auth.config';
import {OAuthService} from "angular-oauth2-oidc";
import { Clipboard } from '@angular/cdk/clipboard';
import {filter} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  constructor(private oauthService: OAuthService, private clipboard: Clipboard) {
    this.oauthService.configure(authCodeFlowConfig);
    this.oauthService.loadDiscoveryDocumentAndLogin();
    this.oauthService.events
      .pipe(filter((e) => e.type === 'token_received'))
      .subscribe((_) => this.oauthService.loadUserProfile());
  }

  get userName(): string {
    const claims = this.oauthService.getIdentityClaims();
    if (!claims) return "";
    return claims['sub' as keyof typeof claims];
  }

  logout() {
    this.oauthService.logOut();
  }

  copyOicToken() {
    const idToken = this.oauthService.getIdToken();
    this.clipboard.copy(idToken);
  }
}

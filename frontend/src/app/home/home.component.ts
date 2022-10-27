import {Component} from '@angular/core';
import {OAuthService} from "angular-oauth2-oidc";
import {filter} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  constructor(private oauthService: OAuthService, private router: Router) {
    this.oauthService.events
      .pipe(filter((e) => e.type === 'token_received'))
      .subscribe((_) => this.router.navigateByUrl('/orders'));
  }

  login() {
    this.oauthService.loadDiscoveryDocumentAndLogin();
  }
}

import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {OAuthService} from "angular-oauth2-oidc";

@Injectable({providedIn: 'root'})
export class AuthRouteGuard implements CanActivate {

  constructor(private oAuthService: OAuthService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    if (this.oAuthService.hasValidIdToken()) {
      return Promise.resolve(true);
    }

    return this.oAuthService.loadDiscoveryDocumentAndTryLogin()
      .then(_ => {
        return this.oAuthService.hasValidIdToken() && this.oAuthService.hasValidAccessToken();
      })
  }
}

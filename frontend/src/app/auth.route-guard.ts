import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {OAuthService} from "angular-oauth2-oidc";

@Injectable({providedIn: 'root'})
export class AuthRouteGuard implements CanActivate {

  constructor(private oAuthService: OAuthService, private router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.oAuthService.hasValidIdToken() && this.oAuthService.hasValidAccessToken()) {
      return true;
    }
    this.router.navigate(['/']);
    return false;
  }
}

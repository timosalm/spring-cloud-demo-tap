import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {EMPTY, Observable, throwError} from "rxjs";
import {OAuthService} from "angular-oauth2-oidc";
import {catchError} from 'rxjs/operators';

@Injectable()
export class AuthHttpInterceptor implements HttpInterceptor {

  constructor(private oauthService: OAuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          this.oauthService.revokeTokenAndLogout();
          location.reload();
          return EMPTY;
        }
        return throwError(error);
      }
    ));
  }
}

import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {EMPTY, Observable, throwError} from "rxjs";
import {catchError} from 'rxjs/operators';
import {Router} from "@angular/router";

@Injectable()
export class AuthHttpInterceptor implements HttpInterceptor {

  constructor(private router: Router) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          this.router.navigate(['/']);
          return EMPTY;
        }
        return throwError(error);
      }
    ));
  }
}

import {Inject, Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class APIBaseURLInterceptor implements HttpInterceptor {
  constructor(@Inject("BASE_API_URL") private baseUrl: string) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const apiReq = request.url.includes(this.baseUrl) ? request : request.clone({url: `${this.baseUrl}${request.url[0] === '/' ? '' : '/'}${request.url}`});
    return next.handle(apiReq);
  }
}

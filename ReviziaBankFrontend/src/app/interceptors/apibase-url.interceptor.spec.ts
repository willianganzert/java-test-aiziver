import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HTTP_INTERCEPTORS, HttpClient} from '@angular/common/http';

import {APIBaseURLInterceptor} from './apibase-url.interceptor';

describe('APIBaseURLInterceptor', () => {
  let httpTestingController: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: "BASE_API_URL",
          useValue: "https://api.example.com"
        },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: APIBaseURLInterceptor,
          multi: true
        }
      ]
    });

    httpTestingController = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should prepend the base URL to requests with relative URLs', () => {
    const relativeUrl = '/users';
    const expectedUrl = 'https://api.example.com/users';

    httpClient.get(relativeUrl).subscribe();

    const req = httpTestingController.expectOne(expectedUrl);
    expect(req.request.method).toEqual('GET');
    req.flush({});
  });

  it('should not modify requests with absolute URLs', () => {
    const absoluteUrl = 'https://api.example.com/users';

    httpClient.get(absoluteUrl).subscribe();

    const req = httpTestingController.expectOne(absoluteUrl);
    expect(req.request.method).toEqual('GET');
    req.flush({});
  });

  it('should not modify requests if they already include the base URL', () => {
    const urlWithBase = 'https://api.example.com/users';

    httpClient.get(urlWithBase).subscribe();

    const req = httpTestingController.expectOne(urlWithBase);
    expect(req.request.method).toEqual('GET');
    req.flush({});
  });
});

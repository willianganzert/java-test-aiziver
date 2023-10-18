import { HttpResponse } from '@angular/common/http';

export function isHttpResponse<T>(object: any): object is HttpResponse<T> {
  return object instanceof HttpResponse;
}

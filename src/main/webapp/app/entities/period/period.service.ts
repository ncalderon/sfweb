import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPeriod } from 'app/shared/model/period.model';

type EntityResponseType = HttpResponse<IPeriod>;
type EntityArrayResponseType = HttpResponse<IPeriod[]>;

@Injectable({ providedIn: 'root' })
export class PeriodService {
  public resourceUrl = SERVER_API_URL + 'api/periods';

  constructor(protected http: HttpClient) {}

  create(period: IPeriod): Observable<EntityResponseType> {
    return this.http.post<IPeriod>(this.resourceUrl, period, { observe: 'response' });
  }

  update(period: IPeriod): Observable<EntityResponseType> {
    return this.http.put<IPeriod>(this.resourceUrl, period, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeriod>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeriod[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

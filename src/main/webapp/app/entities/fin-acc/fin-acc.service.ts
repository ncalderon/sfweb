import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFinAcc } from 'app/shared/model/fin-acc.model';

type EntityResponseType = HttpResponse<IFinAcc>;
type EntityArrayResponseType = HttpResponse<IFinAcc[]>;

@Injectable({ providedIn: 'root' })
export class FinAccService {
  public resourceUrl = SERVER_API_URL + 'api/fin-accs';

  constructor(protected http: HttpClient) {}

  create(finAcc: IFinAcc): Observable<EntityResponseType> {
    return this.http.post<IFinAcc>(this.resourceUrl, finAcc, { observe: 'response' });
  }

  update(finAcc: IFinAcc): Observable<EntityResponseType> {
    return this.http.put<IFinAcc>(this.resourceUrl, finAcc, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFinAcc>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFinAcc[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

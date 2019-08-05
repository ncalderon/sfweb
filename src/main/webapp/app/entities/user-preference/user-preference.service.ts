import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserPreference } from 'app/shared/model/user-preference.model';

type EntityResponseType = HttpResponse<IUserPreference>;
type EntityArrayResponseType = HttpResponse<IUserPreference[]>;

@Injectable({ providedIn: 'root' })
export class UserPreferenceService {
  public resourceUrl = SERVER_API_URL + 'api/user-preferences';

  constructor(protected http: HttpClient) {}

  create(userPreference: IUserPreference): Observable<EntityResponseType> {
    return this.http.post<IUserPreference>(this.resourceUrl, userPreference, { observe: 'response' });
  }

  update(userPreference: IUserPreference): Observable<EntityResponseType> {
    return this.http.put<IUserPreference>(this.resourceUrl, userPreference, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserPreference>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserPreference[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

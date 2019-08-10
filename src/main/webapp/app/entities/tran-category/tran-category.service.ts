import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITranCategory } from 'app/shared/model/tran-category.model';

type EntityResponseType = HttpResponse<ITranCategory>;
type EntityArrayResponseType = HttpResponse<ITranCategory[]>;

@Injectable({ providedIn: 'root' })
export class TranCategoryService {
  public resourceUrl = SERVER_API_URL + 'api/tran-categories';

  constructor(protected http: HttpClient) {}

  create(tranCategory: ITranCategory): Observable<EntityResponseType> {
    return this.http.post<ITranCategory>(this.resourceUrl, tranCategory, { observe: 'response' });
  }

  update(tranCategory: ITranCategory): Observable<EntityResponseType> {
    return this.http.put<ITranCategory>(this.resourceUrl, tranCategory, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITranCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITranCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

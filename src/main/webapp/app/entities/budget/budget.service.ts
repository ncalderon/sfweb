import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBudget } from 'app/shared/model/budget.model';

type EntityResponseType = HttpResponse<IBudget>;
type EntityArrayResponseType = HttpResponse<IBudget[]>;

@Injectable({ providedIn: 'root' })
export class BudgetService {
  public resourceUrl = SERVER_API_URL + 'api/budgets';

  constructor(protected http: HttpClient) {}

  create(budget: IBudget): Observable<EntityResponseType> {
    return this.http.post<IBudget>(this.resourceUrl, budget, { observe: 'response' });
  }

  update(budget: IBudget): Observable<EntityResponseType> {
    return this.http.put<IBudget>(this.resourceUrl, budget, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBudget>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBudget[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

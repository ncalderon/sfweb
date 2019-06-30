import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITranEntry } from 'app/shared/model/tran-entry.model';

type EntityResponseType = HttpResponse<ITranEntry>;
type EntityArrayResponseType = HttpResponse<ITranEntry[]>;

@Injectable({ providedIn: 'root' })
export class TranEntryService {
  public resourceUrl = SERVER_API_URL + 'api/tran-entries';

  constructor(protected http: HttpClient) {}

  create(tranEntry: ITranEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tranEntry);
    return this.http
      .post<ITranEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(tranEntry: ITranEntry): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tranEntry);
    return this.http
      .put<ITranEntry>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITranEntry>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITranEntry[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(tranEntry: ITranEntry): ITranEntry {
    const copy: ITranEntry = Object.assign({}, tranEntry, {
      postDate: tranEntry.postDate != null && tranEntry.postDate.isValid() ? tranEntry.postDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.postDate = res.body.postDate != null ? moment(res.body.postDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((tranEntry: ITranEntry) => {
        tranEntry.postDate = tranEntry.postDate != null ? moment(tranEntry.postDate) : null;
      });
    }
    return res;
  }
}

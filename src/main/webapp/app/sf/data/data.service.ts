import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { ITranEntry } from 'app/shared/model/tran-entry.model';
import { DataTran } from 'app/shared/model/data-transaction.model';

@Injectable({ providedIn: 'root' })
export class DataService {
  public resourceUrl = SERVER_API_URL + 'api/import';

  constructor(protected http: HttpClient) {}

  importTransactions(dataTran: DataTran): Observable<HttpResponse<ITranEntry>> {
    const params: HttpParams = new HttpParams();
    params.set('finAccId', String(dataTran.finAccId));
    params.set('tranFileType', dataTran.tranFileType);
    const formData = new FormData();
    formData.set('file', dataTran.tranFile, dataTran.tranFile.name);
    return this.http.post<ITranEntry>(`${this.resourceUrl}/transaction`, formData, {
      params: params,
      observe: 'response'
    });
  }
}

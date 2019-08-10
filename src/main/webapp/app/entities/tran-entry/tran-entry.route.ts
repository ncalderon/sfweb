import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TranEntry } from 'app/shared/model/tran-entry.model';
import { TranEntryService } from './tran-entry.service';
import { TranEntryComponent } from './tran-entry.component';
import { TranEntryDetailComponent } from './tran-entry-detail.component';
import { TranEntryUpdateComponent } from './tran-entry-update.component';
import { TranEntryDeletePopupComponent } from './tran-entry-delete-dialog.component';
import { ITranEntry } from 'app/shared/model/tran-entry.model';

@Injectable({ providedIn: 'root' })
export class TranEntryResolve implements Resolve<ITranEntry> {
  constructor(private service: TranEntryService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITranEntry> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TranEntry>) => response.ok),
        map((tranEntry: HttpResponse<TranEntry>) => tranEntry.body)
      );
    }
    return of(new TranEntry());
  }
}

export const tranEntryRoute: Routes = [
  {
    path: '',
    component: TranEntryComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'sfwebApp.tranEntry.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TranEntryDetailComponent,
    resolve: {
      tranEntry: TranEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.tranEntry.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TranEntryUpdateComponent,
    resolve: {
      tranEntry: TranEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.tranEntry.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TranEntryUpdateComponent,
    resolve: {
      tranEntry: TranEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.tranEntry.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const tranEntryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TranEntryDeletePopupComponent,
    resolve: {
      tranEntry: TranEntryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.tranEntry.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

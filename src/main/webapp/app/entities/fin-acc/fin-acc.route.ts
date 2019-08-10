import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FinAcc } from 'app/shared/model/fin-acc.model';
import { FinAccService } from './fin-acc.service';
import { FinAccComponent } from './fin-acc.component';
import { FinAccDetailComponent } from './fin-acc-detail.component';
import { FinAccUpdateComponent } from './fin-acc-update.component';
import { FinAccDeletePopupComponent } from './fin-acc-delete-dialog.component';
import { IFinAcc } from 'app/shared/model/fin-acc.model';

@Injectable({ providedIn: 'root' })
export class FinAccResolve implements Resolve<IFinAcc> {
  constructor(private service: FinAccService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFinAcc> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<FinAcc>) => response.ok),
        map((finAcc: HttpResponse<FinAcc>) => finAcc.body)
      );
    }
    return of(new FinAcc());
  }
}

export const finAccRoute: Routes = [
  {
    path: '',
    component: FinAccComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'sfwebApp.finAcc.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FinAccDetailComponent,
    resolve: {
      finAcc: FinAccResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.finAcc.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FinAccUpdateComponent,
    resolve: {
      finAcc: FinAccResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.finAcc.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FinAccUpdateComponent,
    resolve: {
      finAcc: FinAccResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.finAcc.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const finAccPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FinAccDeletePopupComponent,
    resolve: {
      finAcc: FinAccResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.finAcc.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { TranCategory } from 'app/shared/model/tran-category.model';
import { TranCategoryService } from './tran-category.service';
import { TranCategoryComponent } from './tran-category.component';
import { TranCategoryDetailComponent } from './tran-category-detail.component';
import { TranCategoryUpdateComponent } from './tran-category-update.component';
import { TranCategoryDeletePopupComponent } from './tran-category-delete-dialog.component';
import { ITranCategory } from 'app/shared/model/tran-category.model';

@Injectable({ providedIn: 'root' })
export class TranCategoryResolve implements Resolve<ITranCategory> {
  constructor(private service: TranCategoryService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITranCategory> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<TranCategory>) => response.ok),
        map((tranCategory: HttpResponse<TranCategory>) => tranCategory.body)
      );
    }
    return of(new TranCategory());
  }
}

export const tranCategoryRoute: Routes = [
  {
    path: '',
    component: TranCategoryComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'sfwebApp.tranCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TranCategoryDetailComponent,
    resolve: {
      tranCategory: TranCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.tranCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TranCategoryUpdateComponent,
    resolve: {
      tranCategory: TranCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.tranCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TranCategoryUpdateComponent,
    resolve: {
      tranCategory: TranCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.tranCategory.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const tranCategoryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TranCategoryDeletePopupComponent,
    resolve: {
      tranCategory: TranCategoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.tranCategory.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

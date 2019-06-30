import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Budget } from 'app/shared/model/budget.model';
import { BudgetService } from './budget.service';
import { BudgetComponent } from './budget.component';
import { BudgetDetailComponent } from './budget-detail.component';
import { BudgetUpdateComponent } from './budget-update.component';
import { BudgetDeletePopupComponent } from './budget-delete-dialog.component';
import { IBudget } from 'app/shared/model/budget.model';

@Injectable({ providedIn: 'root' })
export class BudgetResolve implements Resolve<IBudget> {
  constructor(private service: BudgetService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBudget> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Budget>) => response.ok),
        map((budget: HttpResponse<Budget>) => budget.body)
      );
    }
    return of(new Budget());
  }
}

export const budgetRoute: Routes = [
  {
    path: '',
    component: BudgetComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'sfwebApp.budget.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BudgetDetailComponent,
    resolve: {
      budget: BudgetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.budget.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BudgetUpdateComponent,
    resolve: {
      budget: BudgetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.budget.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BudgetUpdateComponent,
    resolve: {
      budget: BudgetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.budget.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const budgetPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BudgetDeletePopupComponent,
    resolve: {
      budget: BudgetResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.budget.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

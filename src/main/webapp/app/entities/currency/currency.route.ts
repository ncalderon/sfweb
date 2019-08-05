import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Currency } from 'app/shared/model/currency.model';
import { CurrencyService } from './currency.service';
import { CurrencyComponent } from './currency.component';
import { CurrencyDetailComponent } from './currency-detail.component';
import { CurrencyUpdateComponent } from './currency-update.component';
import { CurrencyDeletePopupComponent } from './currency-delete-dialog.component';
import { ICurrency } from 'app/shared/model/currency.model';

@Injectable({ providedIn: 'root' })
export class CurrencyResolve implements Resolve<ICurrency> {
  constructor(private service: CurrencyService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ICurrency> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Currency>) => response.ok),
        map((currency: HttpResponse<Currency>) => currency.body)
      );
    }
    return of(new Currency());
  }
}

export const currencyRoute: Routes = [
  {
    path: '',
    component: CurrencyComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'sfwebApp.currency.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CurrencyDetailComponent,
    resolve: {
      currency: CurrencyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.currency.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CurrencyUpdateComponent,
    resolve: {
      currency: CurrencyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.currency.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CurrencyUpdateComponent,
    resolve: {
      currency: CurrencyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.currency.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const currencyPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: CurrencyDeletePopupComponent,
    resolve: {
      currency: CurrencyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.currency.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

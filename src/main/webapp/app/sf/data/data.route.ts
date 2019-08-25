import { Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { DataTransactionComponent } from 'app/sf/data/data-transaction.component';

export const dataRoute: Routes = [
  {
    path: 'transaction',
    component: DataTransactionComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.data.transaction.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

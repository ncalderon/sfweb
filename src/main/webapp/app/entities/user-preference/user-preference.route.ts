import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserPreference } from 'app/shared/model/user-preference.model';
import { UserPreferenceService } from './user-preference.service';
import { UserPreferenceComponent } from './user-preference.component';
import { UserPreferenceDetailComponent } from './user-preference-detail.component';
import { UserPreferenceUpdateComponent } from './user-preference-update.component';
import { UserPreferenceDeletePopupComponent } from './user-preference-delete-dialog.component';
import { IUserPreference } from 'app/shared/model/user-preference.model';

@Injectable({ providedIn: 'root' })
export class UserPreferenceResolve implements Resolve<IUserPreference> {
  constructor(private service: UserPreferenceService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUserPreference> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<UserPreference>) => response.ok),
        map((userPreference: HttpResponse<UserPreference>) => userPreference.body)
      );
    }
    return of(new UserPreference());
  }
}

export const userPreferenceRoute: Routes = [
  {
    path: '',
    component: UserPreferenceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'sfwebApp.userPreference.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserPreferenceDetailComponent,
    resolve: {
      userPreference: UserPreferenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.userPreference.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserPreferenceUpdateComponent,
    resolve: {
      userPreference: UserPreferenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.userPreference.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserPreferenceUpdateComponent,
    resolve: {
      userPreference: UserPreferenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.userPreference.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const userPreferencePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UserPreferenceDeletePopupComponent,
    resolve: {
      userPreference: UserPreferenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'sfwebApp.userPreference.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];

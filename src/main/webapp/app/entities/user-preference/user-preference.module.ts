import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SfwebSharedModule } from 'app/shared';
import {
  UserPreferenceComponent,
  UserPreferenceDetailComponent,
  UserPreferenceUpdateComponent,
  UserPreferenceDeletePopupComponent,
  UserPreferenceDeleteDialogComponent,
  userPreferenceRoute,
  userPreferencePopupRoute
} from './';

const ENTITY_STATES = [...userPreferenceRoute, ...userPreferencePopupRoute];

@NgModule({
  imports: [SfwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UserPreferenceComponent,
    UserPreferenceDetailComponent,
    UserPreferenceUpdateComponent,
    UserPreferenceDeleteDialogComponent,
    UserPreferenceDeletePopupComponent
  ],
  entryComponents: [
    UserPreferenceComponent,
    UserPreferenceUpdateComponent,
    UserPreferenceDeleteDialogComponent,
    UserPreferenceDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SfwebUserPreferenceModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

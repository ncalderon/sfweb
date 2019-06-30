import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SfwebSharedModule } from 'app/shared';
import {
  FinAccComponent,
  FinAccDetailComponent,
  FinAccUpdateComponent,
  FinAccDeletePopupComponent,
  FinAccDeleteDialogComponent,
  finAccRoute,
  finAccPopupRoute
} from './';

const ENTITY_STATES = [...finAccRoute, ...finAccPopupRoute];

@NgModule({
  imports: [SfwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [FinAccComponent, FinAccDetailComponent, FinAccUpdateComponent, FinAccDeleteDialogComponent, FinAccDeletePopupComponent],
  entryComponents: [FinAccComponent, FinAccUpdateComponent, FinAccDeleteDialogComponent, FinAccDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SfwebFinAccModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

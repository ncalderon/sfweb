import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SfwebSharedModule } from 'app/shared';
import {
  TranCategoryComponent,
  TranCategoryDetailComponent,
  TranCategoryUpdateComponent,
  TranCategoryDeletePopupComponent,
  TranCategoryDeleteDialogComponent,
  tranCategoryRoute,
  tranCategoryPopupRoute
} from './';

const ENTITY_STATES = [...tranCategoryRoute, ...tranCategoryPopupRoute];

@NgModule({
  imports: [SfwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TranCategoryComponent,
    TranCategoryDetailComponent,
    TranCategoryUpdateComponent,
    TranCategoryDeleteDialogComponent,
    TranCategoryDeletePopupComponent
  ],
  entryComponents: [
    TranCategoryComponent,
    TranCategoryUpdateComponent,
    TranCategoryDeleteDialogComponent,
    TranCategoryDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SfwebTranCategoryModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

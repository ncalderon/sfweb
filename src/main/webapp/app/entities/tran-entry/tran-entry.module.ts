import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SfwebSharedModule } from 'app/shared';
import {
  TranEntryComponent,
  TranEntryDetailComponent,
  TranEntryUpdateComponent,
  TranEntryDeletePopupComponent,
  TranEntryDeleteDialogComponent,
  tranEntryRoute,
  tranEntryPopupRoute
} from './';

const ENTITY_STATES = [...tranEntryRoute, ...tranEntryPopupRoute];

@NgModule({
  imports: [SfwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TranEntryComponent,
    TranEntryDetailComponent,
    TranEntryUpdateComponent,
    TranEntryDeleteDialogComponent,
    TranEntryDeletePopupComponent
  ],
  entryComponents: [TranEntryComponent, TranEntryUpdateComponent, TranEntryDeleteDialogComponent, TranEntryDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SfwebTranEntryModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

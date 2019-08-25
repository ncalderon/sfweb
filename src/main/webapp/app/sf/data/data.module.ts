import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { SfwebSharedModule } from 'app/shared';
import { dataRoute, DataTransactionComponent } from './';

const ENTITY_STATES = [...dataRoute];

@NgModule({
  imports: [SfwebSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [DataTransactionComponent],
  entryComponents: [DataTransactionComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SfwebDataModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}

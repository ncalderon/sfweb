import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SfwebSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [SfwebSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [SfwebSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SfwebSharedModule {
  static forRoot() {
    return {
      ngModule: SfwebSharedModule
    };
  }
}

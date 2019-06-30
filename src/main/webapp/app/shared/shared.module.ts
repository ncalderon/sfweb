import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { SfwebSharedCommonModule, SfLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [SfwebSharedCommonModule],
  declarations: [SfLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [SfLoginModalComponent],
  exports: [SfwebSharedCommonModule, SfLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SfwebSharedModule {
  static forRoot() {
    return {
      ngModule: SfwebSharedModule
    };
  }
}

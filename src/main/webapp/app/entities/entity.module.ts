import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'preference',
        loadChildren: './preference/preference.module#SfwebPreferenceModule'
      },
      {
        path: 'user-preference',
        loadChildren: './user-preference/user-preference.module#SfwebUserPreferenceModule'
      },
      {
        path: 'currency',
        loadChildren: './currency/currency.module#SfwebCurrencyModule'
      },
      {
        path: 'fin-acc',
        loadChildren: './fin-acc/fin-acc.module#SfwebFinAccModule'
      },
      {
        path: 'budget',
        loadChildren: './budget/budget.module#SfwebBudgetModule'
      },
      {
        path: 'period',
        loadChildren: './period/period.module#SfwebPeriodModule'
      },
      {
        path: 'tran-category',
        loadChildren: './tran-category/tran-category.module#SfwebTranCategoryModule'
      },
      {
        path: 'tran-entry',
        loadChildren: './tran-entry/tran-entry.module#SfwebTranEntryModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SfwebEntityModule {}

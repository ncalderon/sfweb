import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'preference',
        loadChildren: () => import('./preference/preference.module').then(m => m.SfwebPreferenceModule)
      },
      {
        path: 'user-preference',
        loadChildren: () => import('./user-preference/user-preference.module').then(m => m.SfwebUserPreferenceModule)
      },
      {
        path: 'currency',
        loadChildren: () => import('./currency/currency.module').then(m => m.SfwebCurrencyModule)
      },
      {
        path: 'fin-acc',
        loadChildren: () => import('./fin-acc/fin-acc.module').then(m => m.SfwebFinAccModule)
      },
      {
        path: 'budget',
        loadChildren: () => import('./budget/budget.module').then(m => m.SfwebBudgetModule)
      },
      {
        path: 'period',
        loadChildren: () => import('./period/period.module').then(m => m.SfwebPeriodModule)
      },
      {
        path: 'tran-category',
        loadChildren: () => import('./tran-category/tran-category.module').then(m => m.SfwebTranCategoryModule)
      },
      {
        path: 'tran-entry',
        loadChildren: () => import('./tran-entry/tran-entry.module').then(m => m.SfwebTranEntryModule)
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

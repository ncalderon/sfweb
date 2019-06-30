import { IBudget } from 'app/shared/model/budget.model';

export interface IPeriod {
  id?: number;
  month?: number;
  budgets?: IBudget[];
}

export class Period implements IPeriod {
  constructor(public id?: number, public month?: number, public budgets?: IBudget[]) {}
}

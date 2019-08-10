export interface IBudget {
  id?: number;
  amount?: number;
  tranCategoryName?: string;
  tranCategoryId?: number;
  periodMonth?: string;
  periodId?: number;
}

export class Budget implements IBudget {
  constructor(
    public id?: number,
    public amount?: number,
    public tranCategoryName?: string,
    public tranCategoryId?: number,
    public periodMonth?: string,
    public periodId?: number
  ) {}
}

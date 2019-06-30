import { ITranEntry } from 'app/shared/model/tran-entry.model';

export interface ITranCategory {
  id?: number;
  name?: string;
  description?: string;
  tranEntries?: ITranEntry[];
  userLogin?: string;
  userId?: number;
  budgetId?: number;
}

export class TranCategory implements ITranCategory {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public tranEntries?: ITranEntry[],
    public userLogin?: string,
    public userId?: number,
    public budgetId?: number
  ) {}
}

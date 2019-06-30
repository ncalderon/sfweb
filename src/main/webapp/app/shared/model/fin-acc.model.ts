import { ITranEntry } from 'app/shared/model/tran-entry.model';

export const enum FinAccStatus {
  INACTIVE = 'INACTIVE',
  ACTIVE = 'ACTIVE'
}

export interface IFinAcc {
  id?: number;
  status?: FinAccStatus;
  accNum?: string;
  name?: string;
  description?: string;
  balance?: number;
  isCreditCard?: boolean;
  billingCycle?: number;
  ccyCode?: string;
  tranEntries?: ITranEntry[];
  userLogin?: string;
  userId?: number;
}

export class FinAcc implements IFinAcc {
  constructor(
    public id?: number,
    public status?: FinAccStatus,
    public accNum?: string,
    public name?: string,
    public description?: string,
    public balance?: number,
    public isCreditCard?: boolean,
    public billingCycle?: number,
    public ccyCode?: string,
    public tranEntries?: ITranEntry[],
    public userLogin?: string,
    public userId?: number
  ) {
    this.isCreditCard = this.isCreditCard || false;
  }
}

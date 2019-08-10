import { Moment } from 'moment';

export const enum TranStatus {
  RECONCILED = 'RECONCILED',
  CLEARED = 'CLEARED',
  UNCLEARED = 'UNCLEARED',
  VOID = 'VOID'
}

export const enum TranType {
  EXPENSE = 'EXPENSE',
  INCOME = 'INCOME'
}

export const enum PaymentMethod {
  UNSPECIFIED = 'UNSPECIFIED',
  CASH = 'CASH',
  CHECK = 'CHECK',
  CREDIT_CARD = 'CREDIT_CARD',
  DEBIT_CARD = 'DEBIT_CARD',
  ELECTRONIC_TRANSFER = 'ELECTRONIC_TRANSFER',
  OTHER = 'OTHER'
}

export interface ITranEntry {
  id?: number;
  tranStatus?: TranStatus;
  tranType?: TranType;
  tranNum?: string;
  refNum?: string;
  postDate?: Moment;
  description?: string;
  amount?: number;
  ccyVal?: number;
  paymentMethod?: PaymentMethod;
  finAccName?: string;
  finAccId?: number;
  tranCategoryName?: string;
  tranCategoryId?: number;
}

export class TranEntry implements ITranEntry {
  constructor(
    public id?: number,
    public tranStatus?: TranStatus,
    public tranType?: TranType,
    public tranNum?: string,
    public refNum?: string,
    public postDate?: Moment,
    public description?: string,
    public amount?: number,
    public ccyVal?: number,
    public paymentMethod?: PaymentMethod,
    public finAccName?: string,
    public finAccId?: number,
    public tranCategoryName?: string,
    public tranCategoryId?: number
  ) {}
}

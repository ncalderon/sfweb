export interface ICurrency {
  id?: number;
  code?: string;
  name?: string;
  userDefault?: boolean;
  jsonval?: string;
}

export class Currency implements ICurrency {
  constructor(public id?: number, public code?: string, public name?: string, public userDefault?: boolean, public jsonval?: string) {
    this.userDefault = this.userDefault || false;
  }
}

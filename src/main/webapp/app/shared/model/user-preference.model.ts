export interface IUserPreference {
  id?: number;
  value?: string;
  userLogin?: string;
  userId?: number;
  preferenceName?: string;
  preferenceId?: number;
}

export class UserPreference implements IUserPreference {
  constructor(
    public id?: number,
    public value?: string,
    public userLogin?: string,
    public userId?: number,
    public preferenceName?: string,
    public preferenceId?: number
  ) {}
}

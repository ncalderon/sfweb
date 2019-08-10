import { IUserPreference } from 'app/shared/model/user-preference.model';

export interface IPreference {
  id?: number;
  name?: string;
  value?: string;
  userPreferences?: IUserPreference[];
}

export class Preference implements IPreference {
  constructor(public id?: number, public name?: string, public value?: string, public userPreferences?: IUserPreference[]) {}
}

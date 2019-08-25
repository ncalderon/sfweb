import { TranFileType } from 'app/shared/model/data.model';

export interface IDataTran {
  finAccId?: number;
  tranFileType?: TranFileType;
  tranFile?: File;
}

export class DataTran implements IDataTran {
  constructor(public finAccId?: number, public tranFileType?: TranFileType, public tranFile?: File) {}
}

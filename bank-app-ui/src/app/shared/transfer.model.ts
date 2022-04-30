import {AccountModel} from "./account.model";

export interface TransferModel{
  id: number;
  type: string;
  amount: number;
  fromAccount: AccountModel;
  toAccount: AccountModel;
  date: Date;
  status: string;
}

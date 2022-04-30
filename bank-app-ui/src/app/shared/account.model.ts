import {CustomerModel} from "./customer.model";

export interface AccountModel{
  id: number;
  customer: CustomerModel;
  balance: number;
  displayName: string;
  openingDate: Date;
  closingDate: Date;
  denomination: string;
  status: string;
}

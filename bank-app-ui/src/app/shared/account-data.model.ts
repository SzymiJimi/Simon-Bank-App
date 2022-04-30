import {CustomerModel} from "./customer.model";
import {TransferModel} from "./transfer.model";

export interface AccountDataModel{
  id: number;
  customer: CustomerModel;
  balance: number;
  transfer: TransferModel;
}

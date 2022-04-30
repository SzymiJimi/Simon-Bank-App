import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Component, Inject} from "@angular/core";
import {TransferModel} from "../shared/transfer.model";

@Component({
  selector: 'transactions-dialog',
  templateUrl: 'transactions-dialog.html',
})
export class TransactionsDialog {

  displayedColumns: string[] = ['id', 'type', 'amount', 'fromAccount', 'toAccount', 'date', 'status'];

  constructor(@Inject(MAT_DIALOG_DATA) public transfers: TransferModel[]) {
  }
}

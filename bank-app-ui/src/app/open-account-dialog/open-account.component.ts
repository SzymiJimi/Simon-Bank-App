import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CustomerModel} from "../shared/customer.model";


@Component({
  selector: 'open-account-dialog',
  templateUrl: 'open-account-dialog.html',
})
export class OpenAccountDialog {

  initialCredit: number = 0.0;

  constructor(
    public dialogRef: MatDialogRef<OpenAccountDialog>,
    @Inject(MAT_DIALOG_DATA) public data: CustomerModel,
  ) {}

  onCancel(): void {
    this.dialogRef.close();
  }
}

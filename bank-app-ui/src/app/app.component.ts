import {Component, OnInit} from '@angular/core';
import {CustomerService} from "./services/customer.service";
import {CustomerModel} from "./shared/customer.model";
import {OpenAccountDialog} from "./open-account-dialog/open-account.component";
import {MatDialog} from "@angular/material/dialog";
import {OpenAccountModel} from "./shared/open-account.model";
import {AccountService} from "./services/account.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AccountDataModel} from "./shared/account-data.model";
import {TransactionsDialog} from "./transactions-dialog/transactions-dialog.component";
import {TransferModel} from "./shared/transfer.model";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'bank-ui';
  errorSnackbarDuration = 5000;
  successSnackbarDuration = 2000;

  displayedColumns: string[] = ['id', 'name', 'surname', 'dateOfBirth', 'actions'];
  customers: CustomerModel[] = [];

  displayedColumnsAccounts: string[] = ['id', 'name', 'surname', 'balance', 'actions'];
  accounts: AccountDataModel[] = [];

  constructor(private customerService: CustomerService,
              private accountService: AccountService,
              public dialog: MatDialog,
              private _snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.loadCustomers();
    this.getAccounts();
  }

  loadCustomers(): void {
    this.customerService.getCustomers().subscribe(
      data => {
        this.customers = data;
      }
    )
  }

  openDialog(customer: CustomerModel): void {
    const dialogRef = this.dialog.open(OpenAccountDialog, {
      width: '450px',
      data: customer,
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.openAccount(customer, result);
      }
    });
  }

  openAccount(customer: CustomerModel, initialCredit: number) {
    let openAccountData: OpenAccountModel = {
      customerId: customer.id,
      initialCredit: initialCredit
    }
    this.accountService.openNewAccount(openAccountData).subscribe(
      {
        next: data => {
          console.log(data);
          this.openSuccessSnackBar();
        },
        error: error => {
          console.log(error);
          this.openErrorSnackBar();
        }
      }
    );
  }

  openErrorSnackBar() {
    this._snackBar.open("Error during account opening! Try again later...", "OK",
      {
        duration: this.errorSnackbarDuration,
        panelClass: ['red-snackbar']
      })
  }

  openSuccessSnackBar() {
    this._snackBar.open("Account opened successfully! Reloading...", "OK",
      {
        duration: this.successSnackbarDuration,
        panelClass: ['green-snackbar']
      })
    setTimeout(function (){
      window.location.reload();
    }, 2000)
  }

  getAccounts(){
    this.accountService.getAccounts().subscribe(
      data => {
        this.accounts = data;
      }
    )
  }

  showTransactions(transactions: TransferModel[]) {
    this.dialog.open(TransactionsDialog, {
      width: '1100px',
      data: transactions,
    });
  }

}

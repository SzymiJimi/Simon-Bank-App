import {Component, OnInit} from '@angular/core';
import {CustomerService} from "./services/customer.service";
import {CustomerModel} from "./shared/customer.model";
import {OpenAccountDialog} from "./open-account-dialog/open-account.component";
import {MatDialog} from "@angular/material/dialog";
import {OpenAccountModel} from "./shared/open-account.model";
import {AccountService} from "./services/account.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  title = 'bank-ui';

  displayedColumns: string[] = ['id', 'name', 'surname', 'dateOfBirth', 'actions'];
  customers: CustomerModel[] = [];

  constructor(private customerService: CustomerService,
              private accountService: AccountService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.loadCustomers();
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
      if(result !== undefined){
        this.openAccount(customer, result);
      }
    });
  }

  openAccount(customer: CustomerModel, initialCredit: number){
    let openAccountData: OpenAccountModel = {
      customerId: customer.id,
      initialCredit: initialCredit
    }
    this.accountService.openNewAccount(openAccountData).subscribe(
      data => console.log(data)
    );
  }

}

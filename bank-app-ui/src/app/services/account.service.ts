import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {environment} from "../../environments/environment";
import {AccountModel} from "../shared/account.model";
import {OpenAccountModel} from "../shared/open-account.model";
import {AccountDataModel} from "../shared/account-data.model";

@Injectable({ providedIn: 'root' })
export class AccountService {

  constructor(protected http: HttpClient) {
  }

  public openNewAccount(openAccountData: OpenAccountModel ): Observable<AccountModel>{
    return this.http.post<AccountModel>(environment.baseUrl +  "/open-account", openAccountData).pipe(
      catchError(this.handleError)
    )

  }

  public handleError(error: HttpErrorResponse){
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }

  public getAccounts(): Observable<AccountDataModel[]>{
    return this.http.get<AccountDataModel[]>(environment.baseUrl +  "/accounts").pipe(
      catchError(this.handleError)
    )

  }
}

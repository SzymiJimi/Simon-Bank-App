import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {CustomerModel} from "../shared/customer.model";

import {catchError, Observable, throwError} from "rxjs";
import {environment} from "../../environments/environment";


@Injectable({ providedIn: 'root' })
export class CustomerService {

  constructor(protected http: HttpClient) {
  }

  public getCustomers(): Observable<CustomerModel[]>{
    return this.http.get<CustomerModel[]>(environment.baseUrl +  "/customers").pipe(
      catchError(this.handleError)
    )

  }

  public handleError(error: HttpErrorResponse){
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }
}

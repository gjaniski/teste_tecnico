import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerDTO } from '../dtos/customer.dto';
import { PaginatedResponse } from '../dtos/paginated-response.dto';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl = '/api/v1/customer';

  constructor(private http: HttpClient) { }

  getCustomersPaginated(page: number, size: number): Observable<PaginatedResponse<CustomerDTO>> {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());

    return this.http.get<PaginatedResponse<CustomerDTO>>(this.apiUrl, { params: params });
  }

  getCustomersByStatusPaginated(customerId: number, page: number, size: number): Observable<PaginatedResponse<CustomerDTO>> {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    params = params.append('size', size.toString());

    return this.http.get<PaginatedResponse<CustomerDTO>>(`${this.apiUrl}/status/${customerId}`, { params: params });
  }

  findCustomerById(customerId: number): Observable<CustomerDTO> {
    return this.http.get<CustomerDTO>(`${this.apiUrl}/${customerId}`);
  }

  createCustomer(customer: CustomerDTO): Observable<CustomerDTO> {
      return this.http.post<CustomerDTO>(this.apiUrl, customer);
  }

  updateCustomer(customerId: number, customer: CustomerDTO): Observable<CustomerDTO> {
    return this.http.put<CustomerDTO>(`${this.apiUrl}/${customerId}`, customer);
  }

  deleteCustomer(customerId: number): Observable<void> {    
    return this.http.delete<void>(`${this.apiUrl}/${customerId}`); 
  }
  
}
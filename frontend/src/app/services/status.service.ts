import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerDTO } from '../dtos/customer.dto';
import { StatusDTO } from '../dtos/status.dto';

@Injectable({
  providedIn: 'root'
})
export class StatusService {

  private apiUrl = '/api/v1/status';

  constructor(private http: HttpClient) { }

  getAllStatus(): Observable<StatusDTO[]> {
    return this.http.get<StatusDTO[]>(this.apiUrl);
  }
  
}
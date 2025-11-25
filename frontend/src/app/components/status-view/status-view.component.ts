import { Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { CustomerDTO } from 'src/app/dtos/customer.dto';
import { PaginatedResponse } from 'src/app/dtos/paginated-response.dto';
import { StatusDTO } from 'src/app/dtos/status.dto';
import { CustomerService } from 'src/app/services/customer.service';

@Component({
  selector: 'app-status-view',
  templateUrl: './status-view.component.html',
  styleUrls: ['./status-view.component.css']
})
export class StatusViewComponent implements OnInit, OnDestroy {
    
  displayedColumns: string[] = ['name'];
  dataSource = new MatTableDataSource<any>([]);
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  totalItems = 0; 
  currentPage = 0; 
  pageSize = 5;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: StatusDTO,
    private dialogRef: MatDialogRef<StatusViewComponent>,
    private customerService: CustomerService,  
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  ngOnDestroy(): void {
  }  

  close() {
    this.dialogRef.close();
  }

  loadCustomers(): void {
    this.customerService.getCustomersByStatusPaginated(this.data.id,this.currentPage, this.pageSize).subscribe({
      next: (response: PaginatedResponse<CustomerDTO>) => {
        this.dataSource.data = response.content;
        this.totalItems = response.page.totalElements;
        
        if (this.dataSource && this.dataSource.paginator) {
          this.dataSource.paginator.length = this.totalItems;
        }
      },
      error: (err) => {
        this.snackBar.open(err.error.message, 'Fechar', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom'
        }); 
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadCustomers();
  }

}

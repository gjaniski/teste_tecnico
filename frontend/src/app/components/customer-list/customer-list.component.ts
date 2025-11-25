import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { CustomerDTO } from 'src/app/dtos/customer.dto';
import { PaginatedResponse } from 'src/app/dtos/paginated-response.dto';
import { CustomerService } from 'src/app/services/customer.service';
import { CustomerDeleteComponent } from '../customer-delete/customer-delete.component';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit, OnDestroy {
  
  dataSource = new MatTableDataSource<CustomerDTO>([])
  displayedColumns: string[] = ['id', 'name', 'email', 'status', 'actions'];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  totalItems = 0; 
  currentPage = 0; 
  pageSize = 5;

  constructor(private customerService: CustomerService, 
    private dialog: MatDialog,  
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.loadCustomers();
  }

  ngOnDestroy(): void {
  }
   
  loadCustomers(): void {
    this.customerService.getCustomersPaginated(this.currentPage, this.pageSize).subscribe({
      next: (response: PaginatedResponse<CustomerDTO>) => { 
        this.dataSource.data = response.content ?? [];
        this.totalItems = response.page?.totalElements ?? 0;

        if ((response.content?.length ?? 0) === 0 && this.currentPage > 0) {
          this.currentPage = 0;
          this.loadCustomers();
        }

        if (this.dataSource && this.dataSource.paginator) {
          this.dataSource.paginator.length = this.totalItems;
        }
      },
      error: (err) => {
        this.snackBar.open('Erro ao listar clientes!', 'Fechar', {
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

  trackByCustomer(index: number, customer: CustomerDTO): number {
    return customer.id;
  }
  
  getCustomerStatusClass(status: string | undefined): string {
    switch (status) {
      case 'ELITE':
        return 'chip-elite';
      case 'PRIME':
        return 'chip-prime';
      case 'VIP':
        return 'chip-vip';
      case 'PREMIUM':
        return 'chip-premium';
      default:
        return '';
    }
  }

  deleteCustomer(element: CustomerDTO): void {
    const dialogRef = this.dialog.open(CustomerDeleteComponent, {
      width: '400px',
      data: element
    });

    dialogRef.afterClosed().subscribe(result => {  
        this.loadCustomers();
    });
  }

}

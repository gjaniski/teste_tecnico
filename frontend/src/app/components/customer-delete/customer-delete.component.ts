import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { CustomerDTO } from 'src/app/dtos/customer.dto';
import { CustomerService } from 'src/app/services/customer.service';

@Component({
  selector: 'app-customer-delete',
  templateUrl: './customer-delete.component.html',
  styleUrls: ['./customer-delete.component.css']
})
export class CustomerDeleteComponent implements OnInit, OnDestroy {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: CustomerDTO,
    private dialogRef: MatDialogRef<CustomerDeleteComponent>,
    private customerService: CustomerService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
  }  

  close() {
    this.dialogRef.close();
  }

  deleteCustomer() {
    this.customerService.deleteCustomer(this.data.id).subscribe({
      next: () => {
        this.snackBar.open('Cliente excluído com sucesso!', 'Fechar', {
          duration: 5000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom'
        });
        this.close();
      },
      error: (err) => {
        this.close();
      }
    });
  }

}
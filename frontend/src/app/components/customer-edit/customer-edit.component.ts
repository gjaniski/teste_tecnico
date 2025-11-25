import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerDTO } from 'src/app/dtos/customer.dto';
import { StatusDTO } from 'src/app/dtos/status.dto';
import { CustomerService } from 'src/app/services/customer.service';
import { StatusService } from 'src/app/services/status.service';

@Component({
  selector: 'app-customer-edit',
  templateUrl: './customer-edit.component.html',
  styleUrls: ['./customer-edit.component.css']
})
export class CustomerEditComponent implements OnInit, OnDestroy {

  form!: FormGroup;
  isEditMode = false;
  customerId!: number;

  statusList: StatusDTO[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private customerService: CustomerService,
    private statusService: StatusService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.createForm();
    this.loadStatus();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.customerId = Number(idParam);
    
      const emailControl = this.form.get('email');
    
      if (emailControl) {
        emailControl.disable(); 
      }
    }
  }

  ngOnDestroy(): void {
  }

  createForm() {
    this.form = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      status: ['', []]
    });
  }

  loadCustomer(id: number): void {    
    this.customerService.findCustomerById(id).subscribe({
        next: (customer: CustomerDTO) => {
            const dataToPatch = {
                id: customer.id,
                name: customer.name,
                email: customer.email,
                status: customer.status
            };

            this.form.patchValue(dataToPatch);
        },
        error: (err) => {
          this.snackBar.open('Erro ao carregar cliente!', 'Fechar', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom'
          }); 
        }
    });
  }

  loadStatus(): void {    
    this.statusService.getAllStatus().subscribe({
        next: (status: StatusDTO[]) => {
            this.statusList = status;

            if (this.isEditMode) {
                this.loadCustomer(this.customerId); 
            }
        },
        error: (err) => {
          this.snackBar.open('Erro ao carregar lista de status!', 'Fechar', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom'
          }); 
        }
    });
  }

  compareStatus(status1: StatusDTO, status2: StatusDTO): boolean {
    return status1 && status2 ? status1.id === status2.id : status1 === status2;
  }
  
  save() {
    if (this.form.invalid) return;

    const customerData = this.form.getRawValue();
    
    if (typeof customerData.status === 'string' && customerData.status === '') {
        customerData.status = [];
    }

    if (this.isEditMode) {
      customerData.id = this.customerId;
    }

    if (this.isEditMode) {
      this.customerService.updateCustomer(this.customerId, customerData).subscribe({
        next: (response) => {
          this.snackBar.open('Cliente atualizado com sucesso!', 'Fechar', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom'
          }); 
          this.returnToList();
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
    else {
      this.customerService.createCustomer(customerData).subscribe({
        next: (response) => {
          this.snackBar.open('Cliente criado com sucesso!', 'Fechar', {
            duration: 5000,
            horizontalPosition: 'center',
            verticalPosition: 'bottom'
          }); 
          this.returnToList();
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
  }

  returnToList() {
    this.router.navigate(['/customer']);
  }

}

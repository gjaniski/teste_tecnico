import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { StatusDTO } from 'src/app/dtos/status.dto';
import { StatusViewComponent } from '../status-view/status-view.component';
import { StatusService } from 'src/app/services/status.service';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit, OnDestroy {
  
  dataSource = new MatTableDataSource<StatusDTO>([])
  displayedColumns: string[] = ['name', 'actions'];

  constructor(private dialog: MatDialog, private statusService: StatusService) {}

  ngOnInit(): void {
    this.loadStatus();
  }

  ngOnDestroy(): void {
  }
   
  loadStatus(): void {
    this.statusService.getAllStatus().subscribe({
        next: (status: StatusDTO[]) => {
          this.dataSource = new MatTableDataSource(status);
      },
      error: (err) => {
        console.error('Erro ao buscar clientes paginados:', err);
      }
    });
  }

  trackByStatus(index: number, customer: StatusDTO): number {
    return customer.id as number;
  }
  
  getNameClass(name: string | undefined): string {
    switch (name) {
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

  openModal(element: StatusDTO) {
    this.dialog.open(StatusViewComponent, {
      width: '700px',
      data: element
    });
  }

}

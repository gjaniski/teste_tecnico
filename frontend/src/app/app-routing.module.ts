import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerListComponent } from './components/customer-list/customer-list.component';
import { StatusComponent } from './components/status/status.component';
import { CustomerEditComponent } from './components/customer-edit/customer-edit.component';

const routes: Routes = [  {
    path: 'customer',
    children: [
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      { path: 'list', component: CustomerListComponent },
      { path: 'new', component: CustomerEditComponent },
      { path: 'edit/:id', component: CustomerEditComponent }
    ]
  },
  { path: 'status', component: StatusComponent },
  { path: '', redirectTo: 'customer', pathMatch: 'full' },
  { path: '**', redirectTo: 'customer' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

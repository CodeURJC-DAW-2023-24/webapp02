import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './main.component';
import { LoginComponent } from './login.component';
import { AdminComponent } from './admin.component';


const appRoutes: Routes = [
  { path: 'Admin', component: AdminComponent},
  { path: 'Main', component: MainComponent},
  { path: 'Login', component: LoginComponent},
  { path: '', redirectTo: 'Admin', pathMatch: 'full' }
]

export const routing = RouterModule.forRoot(appRoutes);

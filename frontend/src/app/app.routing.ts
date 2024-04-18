import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './main.component';
import { LoginComponent } from './login.component';


const appRoutes: Routes = [
  { path: 'Main', component: MainComponent},
  { path: 'Login', component: LoginComponent},
  { path: '', redirectTo: 'main', pathMatch: 'full' }
]

export const routing = RouterModule.forRoot(appRoutes);

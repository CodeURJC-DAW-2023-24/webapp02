import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './main.component';
import { LoginComponent } from './login.component';
import { ProfileComponent } from './profile.component';


const appRoutes: Routes = [
  { path: 'Main', component: MainComponent},
  { path: 'Login', component: LoginComponent},
  {path: 'Profile', component: ProfileComponent},
  { path: '', redirectTo: 'main', pathMatch: 'full' }
]

export const routing = RouterModule.forRoot(appRoutes);

import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './components/main.component';
import { LoginComponent } from './components/login.component';
import { LibraryComponent } from './components/library.component';


const appRoutes: Routes = [
  { path: 'Main', component: MainComponent},
  { path: 'Login', component: LoginComponent},
  { path: 'Library/:type', component: LibraryComponent },
  { path: '', redirectTo: 'Login', pathMatch: 'full' }
]

export const routing = RouterModule.forRoot(appRoutes);

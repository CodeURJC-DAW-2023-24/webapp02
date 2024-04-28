import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './components/main.component';
import { LoginComponent } from './components/login.component';
import { LibraryComponent } from './components/library.component';
import { SingleElementComponent } from './components/singleElement.component';
import { signInComponent} from './components/signIn.component';
import { AdminComponent } from './components/admin.component';


const appRoutes: Routes = [
  { path: 'Admin', component: AdminComponent},
  { path: 'Main', component: MainComponent},
  { path: 'Login', component: LoginComponent},
  { path: 'Library/:type', component: LibraryComponent },
  { path: 'SingleElement/:id', component: SingleElementComponent },
  {path: 'SignIn', component: signInComponent},
  { path: '', redirectTo: 'Login', pathMatch: 'full' }
]

export const routing = RouterModule.forRoot(appRoutes);

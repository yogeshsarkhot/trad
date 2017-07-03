import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './providers/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private authActionText: string = '';

  constructor(private auth: AuthService, private router: Router) {
    this.router.navigate(['splash']);
  }

  ngOnInit() {
    this.auth.getAuthActionText().subscribe(data => {
      this.authActionText = data['authActionText'];
    });
  }

  processAuthAction(): void {
    this.router.navigate(['login']);
  }
}

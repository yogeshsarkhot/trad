import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../providers/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  private userName: string = '';
  private userPwd: string = '';
  private loginMsg: string = '';

  constructor(private auth: AuthService, private router: Router) { }

  ngOnInit() {}

  cancel(): void {
    this.router.navigate(['splash']);
  }

  login(): void {
    if (this.userName == '' || this.userPwd == '') {
      this.loginMsg = 'UserName and Password required.';
    } else {
      this.auth.login(this.userName, this.userPwd).subscribe(data => {
        if ((data['success']) == true) {
          // TBD: Set authAction = 'Logout';
          this.loginMsg = '';
          this.router.navigate(['dashboard']);
        } else {
          this.userName = '';
          this.userPwd = '';
          this.loginMsg = 'Login failed. Please check your credentials.';
        }
      });
    }
  }
}

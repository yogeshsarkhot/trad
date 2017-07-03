import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../providers/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  private userName: string = '';
  private userPwd: string = '';
  private userPwdConfirm: string = '';
  private userEmail: string = '';
  private registerMsg: string = '';

  constructor(private auth: AuthService, private router: Router) { }

  ngOnInit() {}

  cancel(): void {
    this.router.navigate(['splash']);
  }

  register(): void {
    if (this.userName == '' || this.userPwd == '' || this.userEmail == '') {
      this.registerMsg = 'Please complete all fields.';
    } else {
      if (this.userPwd != this.userPwdConfirm) {
        this.userPwd = '';
        this.userPwdConfirm = '';
        this.registerMsg = 'Passwords must match.';
      } else {
        this.auth.register(this.userName, this.userPwd, this.userEmail).subscribe(data => {
          if ((data['success']) == true) {
            this.registerMsg = '';
            this.router.navigate(['login']);
          } else {
            this.userName = '';
            this.userPwd = '';
            this.userPwdConfirm = '';
            this.userEmail = '';
            this.registerMsg = 'Registration failed. Lorem Ipsum...';
          }
        });
      }
    }
  }
}

import { Injectable } from '@angular/core';
import {Http} from '@angular/http';
import {Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/observable/of';

@Injectable()
export class AuthService {
  private authActionText: string = 'Login';

  //private loginEndpoint: string = '/';
  //private registerEndpoint: string = '/';

  private headers = new Headers({ 'Content-Type': 'application/json' });
  private options = new RequestOptions({ headers: this.headers });

  constructor(private http: Http) { }

  login(userName: string, userPwd: string) : Observable<any> {
    /*
     return this.http.post(this.loginEndpoint, {user_name : userName, user_password : userPwd}, this.options)
     .map(res => <any[]>res.json());
     */

    // For testing, until Spring Service is implemented
    let testUserName: string = 'User';
    let testUserPwd: string = 'password';

    let success: boolean = false;

    if (userName == testUserName && userPwd == testUserPwd) {
      success = true;
      this.authActionText = 'Logout';
    }

    return Observable.of({"success" : success});
  }

  register(userName: string, userPwd: string, userEmail: string) : Observable<any> {
    /*
     return this.http.post(this.registerEndpoint, {user_name : userName, user_password : userPwd, user_email: userEmail}, this.options)
     .map(res => <any[]>res.json());
     */

    // For testing, until Spring Service is implemented
    let testUserName: string = 'FooBar';
    let testUserPwd: string = 'password';
    let testUserEmail: string = 'foobar@test.com';

    let success: boolean = true;

    if (userName == testUserName && userPwd == testUserPwd && userEmail == testUserEmail) {
      success = false;
    }

    return Observable.of({"success" : success});
  }

  logout(): void {
    this.authActionText = 'Login';
  }

  getAuthActionText():  Observable<any> {
    return Observable.of({"authActionText" : this.authActionText});
  }
}

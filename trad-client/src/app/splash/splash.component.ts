import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-splash',
  templateUrl: './splash.component.html',
  styleUrls: ['./splash.component.css']
})
export class SplashComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {}

  register() : void {
    this.router.navigate(['register']);
  }

  login(): void {
    this.router.navigate(['login']);
  }
}

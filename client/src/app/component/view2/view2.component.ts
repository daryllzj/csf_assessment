import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { View2 } from 'src/app/model/view2';
import { HttpService } from 'src/app/services/http.service';

@Component({
  selector: 'app-view2',
  templateUrl: './view2.component.html',
  styleUrls: ['./view2.component.css']
})
export class View2Component implements OnInit , OnDestroy{

  param$!: Subscription

  bundleId!: string

  title!: string
  name!: string
  comments!: string
  date!: string
  url!: string[]

  constructor(private activatedRoute: ActivatedRoute, private http: HttpService, private router: Router){}


  ngOnInit(): void {

    this.param$ = this.activatedRoute.params.subscribe(
      (params)=> {
        this.bundleId = params['bundleId'];
        console.log("bundleId>", this.bundleId);

        this.http.getBundleById(this.bundleId)
        .then((data: View2) => {
          this.title = data['title']
          this.name = data['name']
          this.comments = data['comments']
          this.date = data['date']
          this.url = data['url']
        })
        
      })
    
  }

  ngOnDestroy(): void {
      this.param$.unsubscribe()
  }

  back(){
    this.router.navigate(['/view1']);
  }
}

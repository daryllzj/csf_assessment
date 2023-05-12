import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { View0 } from 'src/app/model/view0';
import { View2 } from 'src/app/model/view2';
import { HttpService } from 'src/app/services/http.service';

@Component({
  selector: 'app-view0',
  templateUrl: './view0.component.html',
  styleUrls: ['./view0.component.css']
})
export class View0Component implements OnInit {

  constructor( private http: HttpService, private router: Router){}

  title!: string
  date!: string
  bundles!: View0[]

  ngOnInit(): void {
      this.http.getBundles()
      .then((data: View0[]) => {
        // const bundleId = data['bundleId']
      this.bundles = data
      })
  }

  gotoView1(){
    this.router.navigate(['/view1']);
  }

}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { lastValueFrom } from 'rxjs';
import { Bundle } from '../model/bundle';
import { View2 } from '../model/view2';
import { View0} from '../model/view0';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  constructor(private http: HttpClient) { }

  uploadFile(formData: FormData){
    console.log("formData received> ", formData)
    return lastValueFrom(this.http.post<Bundle>("/upload", formData))
  }

  getBundleById(bundleId: string){
    console.log("bundleId received> ", bundleId)
    return lastValueFrom(this.http.get<View2>("/bundle/" + bundleId))
  }

  getBundles(){
    return lastValueFrom(this.http.get<View0[]>("/bundles"))
  }
}

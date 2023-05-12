import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Bundle } from 'src/app/model/bundle';
import { HttpService } from 'src/app/services/http.service';

@Component({
  selector: 'app-view1',
  templateUrl: './view1.component.html',
  styleUrls: ['./view1.component.css']
})
export class View1Component implements OnInit {

  @ViewChild('file') fileInput!: ElementRef;

  form!: FormGroup

  comments: string = ""

  constructor(private fb: FormBuilder, private httpService: HttpService, private router: Router){}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: this.fb.control('',[Validators.required]),
      title: this.fb.control('', [Validators.required]),
      comments: this.fb.control(''),
      file: this.fb.control('', [Validators.required]),
    })
  }

  uploadFile(){
    const name = this.form.value['name']
    const title = this.form.value['title']
    let comment = this.form.value['comments']
    let length = comment.length
    console.log("length> ",length)
    if (length > 0) {
      this.comments= this.form.value['comments']
    } else {
      this.comments = "no comments given"
    }
    const file = this.fileInput.nativeElement.files[0];
    const fileName = file.name

    console.log("name> ",name)
    console.log("title> ",title)
    console.log("comments> ", this.comments)
    console.log("file> ", file)
    console.log("fileName> ", fileName)

    const formData = new FormData()
    formData.set("name", name)
    formData.set("title", title)
    formData.set("comments", this.comments)
    formData.set("file", file)
    formData.set("fileName", fileName)

    console.log("formData> ", formData)

    this.httpService.uploadFile(formData)
    .then((data: Bundle) => {
      const bundleId = data['bundleId']
      this.router.navigate(['/view2', bundleId]);
    })
  }

  cancel(){
    this.router.navigate(['/']);
  }

}

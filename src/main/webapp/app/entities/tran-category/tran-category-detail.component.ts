import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITranCategory } from 'app/shared/model/tran-category.model';

@Component({
  selector: 'sf-tran-category-detail',
  templateUrl: './tran-category-detail.component.html'
})
export class TranCategoryDetailComponent implements OnInit {
  tranCategory: ITranCategory;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tranCategory }) => {
      this.tranCategory = tranCategory;
    });
  }

  previousState() {
    window.history.back();
  }
}

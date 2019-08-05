import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITranEntry } from 'app/shared/model/tran-entry.model';

@Component({
  selector: 'sf-tran-entry-detail',
  templateUrl: './tran-entry-detail.component.html'
})
export class TranEntryDetailComponent implements OnInit {
  tranEntry: ITranEntry;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tranEntry }) => {
      this.tranEntry = tranEntry;
    });
  }

  previousState() {
    window.history.back();
  }
}

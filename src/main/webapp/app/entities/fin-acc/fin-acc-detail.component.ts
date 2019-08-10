import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFinAcc } from 'app/shared/model/fin-acc.model';

@Component({
  selector: 'sf-fin-acc-detail',
  templateUrl: './fin-acc-detail.component.html'
})
export class FinAccDetailComponent implements OnInit {
  finAcc: IFinAcc;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ finAcc }) => {
      this.finAcc = finAcc;
    });
  }

  previousState() {
    window.history.back();
  }
}

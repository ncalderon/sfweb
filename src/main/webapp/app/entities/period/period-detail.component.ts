import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPeriod } from 'app/shared/model/period.model';

@Component({
  selector: 'sf-period-detail',
  templateUrl: './period-detail.component.html'
})
export class PeriodDetailComponent implements OnInit {
  period: IPeriod;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ period }) => {
      this.period = period;
    });
  }

  previousState() {
    window.history.back();
  }
}

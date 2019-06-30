import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserPreference } from 'app/shared/model/user-preference.model';

@Component({
  selector: 'sf-user-preference-detail',
  templateUrl: './user-preference-detail.component.html'
})
export class UserPreferenceDetailComponent implements OnInit {
  userPreference: IUserPreference;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userPreference }) => {
      this.userPreference = userPreference;
    });
  }

  previousState() {
    window.history.back();
  }
}

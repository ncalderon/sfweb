import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IPeriod, Period } from 'app/shared/model/period.model';
import { PeriodService } from './period.service';

@Component({
  selector: 'sf-period-update',
  templateUrl: './period-update.component.html'
})
export class PeriodUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    month: [null, [Validators.required]]
  });

  constructor(protected periodService: PeriodService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ period }) => {
      this.updateForm(period);
    });
  }

  updateForm(period: IPeriod) {
    this.editForm.patchValue({
      id: period.id,
      month: period.month
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const period = this.createFromForm();
    if (period.id !== undefined) {
      this.subscribeToSaveResponse(this.periodService.update(period));
    } else {
      this.subscribeToSaveResponse(this.periodService.create(period));
    }
  }

  private createFromForm(): IPeriod {
    return {
      ...new Period(),
      id: this.editForm.get(['id']).value,
      month: this.editForm.get(['month']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriod>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}

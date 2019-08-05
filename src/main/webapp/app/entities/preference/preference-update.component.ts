import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IPreference, Preference } from 'app/shared/model/preference.model';
import { PreferenceService } from './preference.service';

@Component({
  selector: 'sf-preference-update',
  templateUrl: './preference-update.component.html'
})
export class PreferenceUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)]],
    value: [null, [Validators.required]]
  });

  constructor(protected preferenceService: PreferenceService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ preference }) => {
      this.updateForm(preference);
    });
  }

  updateForm(preference: IPreference) {
    this.editForm.patchValue({
      id: preference.id,
      name: preference.name,
      value: preference.value
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const preference = this.createFromForm();
    if (preference.id !== undefined) {
      this.subscribeToSaveResponse(this.preferenceService.update(preference));
    } else {
      this.subscribeToSaveResponse(this.preferenceService.create(preference));
    }
  }

  private createFromForm(): IPreference {
    return {
      ...new Preference(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      value: this.editForm.get(['value']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPreference>>) {
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

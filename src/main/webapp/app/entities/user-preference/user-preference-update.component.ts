import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IUserPreference, UserPreference } from 'app/shared/model/user-preference.model';
import { UserPreferenceService } from './user-preference.service';
import { IUser, UserService } from 'app/core';
import { IPreference } from 'app/shared/model/preference.model';
import { PreferenceService } from 'app/entities/preference';

@Component({
  selector: 'sf-user-preference-update',
  templateUrl: './user-preference-update.component.html'
})
export class UserPreferenceUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  preferences: IPreference[];

  editForm = this.fb.group({
    id: [],
    value: [null, [Validators.required]],
    userId: [null, Validators.required],
    preferenceId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userPreferenceService: UserPreferenceService,
    protected userService: UserService,
    protected preferenceService: PreferenceService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userPreference }) => {
      this.updateForm(userPreference);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.preferenceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPreference[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPreference[]>) => response.body)
      )
      .subscribe((res: IPreference[]) => (this.preferences = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userPreference: IUserPreference) {
    this.editForm.patchValue({
      id: userPreference.id,
      value: userPreference.value,
      userId: userPreference.userId,
      preferenceId: userPreference.preferenceId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userPreference = this.createFromForm();
    if (userPreference.id !== undefined) {
      this.subscribeToSaveResponse(this.userPreferenceService.update(userPreference));
    } else {
      this.subscribeToSaveResponse(this.userPreferenceService.create(userPreference));
    }
  }

  private createFromForm(): IUserPreference {
    return {
      ...new UserPreference(),
      id: this.editForm.get(['id']).value,
      value: this.editForm.get(['value']).value,
      userId: this.editForm.get(['userId']).value,
      preferenceId: this.editForm.get(['preferenceId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserPreference>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackPreferenceById(index: number, item: IPreference) {
    return item.id;
  }
}

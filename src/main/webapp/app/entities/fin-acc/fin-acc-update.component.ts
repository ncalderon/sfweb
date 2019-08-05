import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { FinAcc, IFinAcc } from 'app/shared/model/fin-acc.model';
import { FinAccService } from './fin-acc.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'sf-fin-acc-update',
  templateUrl: './fin-acc-update.component.html'
})
export class FinAccUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    status: [null, [Validators.required]],
    accNum: [null, [Validators.minLength(4), Validators.maxLength(64)]],
    name: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)]],
    description: [null, [Validators.maxLength(256)]],
    balance: [null, [Validators.required]],
    creditCard: [],
    billingCycle: [],
    ccyCode: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)]],
    userId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected finAccService: FinAccService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ finAcc }) => {
      this.updateForm(finAcc);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(finAcc: IFinAcc) {
    this.editForm.patchValue({
      id: finAcc.id,
      status: finAcc.status,
      accNum: finAcc.accNum,
      name: finAcc.name,
      description: finAcc.description,
      balance: finAcc.balance,
      creditCard: finAcc.creditCard,
      billingCycle: finAcc.billingCycle,
      ccyCode: finAcc.ccyCode,
      userId: finAcc.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const finAcc = this.createFromForm();
    if (finAcc.id !== undefined) {
      this.subscribeToSaveResponse(this.finAccService.update(finAcc));
    } else {
      this.subscribeToSaveResponse(this.finAccService.create(finAcc));
    }
  }

  private createFromForm(): IFinAcc {
    return {
      ...new FinAcc(),
      id: this.editForm.get(['id']).value,
      status: this.editForm.get(['status']).value,
      accNum: this.editForm.get(['accNum']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      balance: this.editForm.get(['balance']).value,
      creditCard: this.editForm.get(['creditCard']).value,
      billingCycle: this.editForm.get(['billingCycle']).value,
      ccyCode: this.editForm.get(['ccyCode']).value,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinAcc>>) {
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
}

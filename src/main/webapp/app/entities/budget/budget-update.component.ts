import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IBudget, Budget } from 'app/shared/model/budget.model';
import { BudgetService } from './budget.service';
import { ITranCategory } from 'app/shared/model/tran-category.model';
import { TranCategoryService } from 'app/entities/tran-category';
import { IPeriod } from 'app/shared/model/period.model';
import { PeriodService } from 'app/entities/period';

@Component({
  selector: 'sf-budget-update',
  templateUrl: './budget-update.component.html'
})
export class BudgetUpdateComponent implements OnInit {
  isSaving: boolean;

  trancategories: ITranCategory[];

  periods: IPeriod[];

  editForm = this.fb.group({
    id: [],
    amount: [null, [Validators.required]],
    tranCategoryId: [],
    periodId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected budgetService: BudgetService,
    protected tranCategoryService: TranCategoryService,
    protected periodService: PeriodService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ budget }) => {
      this.updateForm(budget);
    });
    this.tranCategoryService
      .query({ filter: 'budget-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<ITranCategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITranCategory[]>) => response.body)
      )
      .subscribe(
        (res: ITranCategory[]) => {
          if (!!this.editForm.get('tranCategoryId').value) {
            this.trancategories = res;
          } else {
            this.tranCategoryService
              .find(this.editForm.get('tranCategoryId').value)
              .pipe(
                filter((subResMayBeOk: HttpResponse<ITranCategory>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<ITranCategory>) => subResponse.body)
              )
              .subscribe(
                (subRes: ITranCategory) => (this.trancategories = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.periodService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IPeriod[]>) => mayBeOk.ok),
        map((response: HttpResponse<IPeriod[]>) => response.body)
      )
      .subscribe((res: IPeriod[]) => (this.periods = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(budget: IBudget) {
    this.editForm.patchValue({
      id: budget.id,
      amount: budget.amount,
      tranCategoryId: budget.tranCategoryId,
      periodId: budget.periodId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const budget = this.createFromForm();
    if (budget.id !== undefined) {
      this.subscribeToSaveResponse(this.budgetService.update(budget));
    } else {
      this.subscribeToSaveResponse(this.budgetService.create(budget));
    }
  }

  private createFromForm(): IBudget {
    return {
      ...new Budget(),
      id: this.editForm.get(['id']).value,
      amount: this.editForm.get(['amount']).value,
      tranCategoryId: this.editForm.get(['tranCategoryId']).value,
      periodId: this.editForm.get(['periodId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBudget>>) {
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

  trackTranCategoryById(index: number, item: ITranCategory) {
    return item.id;
  }

  trackPeriodById(index: number, item: IPeriod) {
    return item.id;
  }
}

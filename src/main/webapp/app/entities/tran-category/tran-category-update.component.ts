import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITranCategory, TranCategory } from 'app/shared/model/tran-category.model';
import { TranCategoryService } from './tran-category.service';
import { IUser, UserService } from 'app/core';
import { IBudget } from 'app/shared/model/budget.model';
import { BudgetService } from 'app/entities/budget';

@Component({
  selector: 'sf-tran-category-update',
  templateUrl: './tran-category-update.component.html'
})
export class TranCategoryUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  budgets: IBudget[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)]],
    description: [null, [Validators.maxLength(256)]],
    userId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tranCategoryService: TranCategoryService,
    protected userService: UserService,
    protected budgetService: BudgetService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ tranCategory }) => {
      this.updateForm(tranCategory);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.budgetService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBudget[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBudget[]>) => response.body)
      )
      .subscribe((res: IBudget[]) => (this.budgets = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(tranCategory: ITranCategory) {
    this.editForm.patchValue({
      id: tranCategory.id,
      name: tranCategory.name,
      description: tranCategory.description,
      userId: tranCategory.userId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const tranCategory = this.createFromForm();
    if (tranCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.tranCategoryService.update(tranCategory));
    } else {
      this.subscribeToSaveResponse(this.tranCategoryService.create(tranCategory));
    }
  }

  private createFromForm(): ITranCategory {
    return {
      ...new TranCategory(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      userId: this.editForm.get(['userId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITranCategory>>) {
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

  trackBudgetById(index: number, item: IBudget) {
    return item.id;
  }
}

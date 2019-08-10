import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { ITranEntry, TranEntry } from 'app/shared/model/tran-entry.model';
import { TranEntryService } from './tran-entry.service';
import { IFinAcc } from 'app/shared/model/fin-acc.model';
import { FinAccService } from 'app/entities/fin-acc';
import { ITranCategory } from 'app/shared/model/tran-category.model';
import { TranCategoryService } from 'app/entities/tran-category';

@Component({
  selector: 'sf-tran-entry-update',
  templateUrl: './tran-entry-update.component.html'
})
export class TranEntryUpdateComponent implements OnInit {
  isSaving: boolean;

  finaccs: IFinAcc[];

  trancategories: ITranCategory[];
  postDateDp: any;

  editForm = this.fb.group({
    id: [],
    tranStatus: [null, [Validators.required]],
    tranType: [null, [Validators.required]],
    tranNum: [],
    refNum: [],
    postDate: [null, [Validators.required]],
    description: [null, [Validators.maxLength(256)]],
    amount: [null, [Validators.required]],
    ccyVal: [null, [Validators.required]],
    paymentMethod: [],
    finAccId: [null, Validators.required],
    tranCategoryId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tranEntryService: TranEntryService,
    protected finAccService: FinAccService,
    protected tranCategoryService: TranCategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ tranEntry }) => {
      this.updateForm(tranEntry);
    });
    this.finAccService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IFinAcc[]>) => mayBeOk.ok),
        map((response: HttpResponse<IFinAcc[]>) => response.body)
      )
      .subscribe((res: IFinAcc[]) => (this.finaccs = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.tranCategoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITranCategory[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITranCategory[]>) => response.body)
      )
      .subscribe((res: ITranCategory[]) => (this.trancategories = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(tranEntry: ITranEntry) {
    this.editForm.patchValue({
      id: tranEntry.id,
      tranStatus: tranEntry.tranStatus,
      tranType: tranEntry.tranType,
      tranNum: tranEntry.tranNum,
      refNum: tranEntry.refNum,
      postDate: tranEntry.postDate,
      description: tranEntry.description,
      amount: tranEntry.amount,
      ccyVal: tranEntry.ccyVal,
      paymentMethod: tranEntry.paymentMethod,
      finAccId: tranEntry.finAccId,
      tranCategoryId: tranEntry.tranCategoryId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const tranEntry = this.createFromForm();
    if (tranEntry.id !== undefined) {
      this.subscribeToSaveResponse(this.tranEntryService.update(tranEntry));
    } else {
      this.subscribeToSaveResponse(this.tranEntryService.create(tranEntry));
    }
  }

  private createFromForm(): ITranEntry {
    return {
      ...new TranEntry(),
      id: this.editForm.get(['id']).value,
      tranStatus: this.editForm.get(['tranStatus']).value,
      tranType: this.editForm.get(['tranType']).value,
      tranNum: this.editForm.get(['tranNum']).value,
      refNum: this.editForm.get(['refNum']).value,
      postDate: this.editForm.get(['postDate']).value,
      description: this.editForm.get(['description']).value,
      amount: this.editForm.get(['amount']).value,
      ccyVal: this.editForm.get(['ccyVal']).value,
      paymentMethod: this.editForm.get(['paymentMethod']).value,
      finAccId: this.editForm.get(['finAccId']).value,
      tranCategoryId: this.editForm.get(['tranCategoryId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITranEntry>>) {
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

  trackFinAccById(index: number, item: IFinAcc) {
    return item.id;
  }

  trackTranCategoryById(index: number, item: ITranCategory) {
    return item.id;
  }
}

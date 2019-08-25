import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { AccountService } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FinAccService } from 'app/entities/fin-acc';
import { filter, map } from 'rxjs/operators';
import { IFinAcc } from 'app/shared/model/fin-acc.model';
import { ITranEntry, TranEntry } from 'app/shared/model/tran-entry.model';

@Component({
  selector: 'sf-data-transaction',
  templateUrl: './data-transaction.component.html'
})
export class DataTransactionComponent implements OnInit {
  isSaving: boolean;

  finaccs: IFinAcc[];

  constructor(
    protected finAccService: FinAccService,
    protected parseLinks: JhiParseLinks,
    protected jhiAlertService: JhiAlertService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager
  ) {}

  loadAll() {
    this.finAccService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IFinAcc[]>) => mayBeOk.ok),
        map((response: HttpResponse<IFinAcc[]>) => response.body)
      )
      .subscribe((res: IFinAcc[]) => (this.finaccs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  ngOnInit(): void {
    this.isSaving = false;
    this.loadAll();
  }

  save() {}

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
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
}

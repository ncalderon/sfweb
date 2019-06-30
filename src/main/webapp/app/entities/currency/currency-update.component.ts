import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ICurrency, Currency } from 'app/shared/model/currency.model';
import { CurrencyService } from './currency.service';

@Component({
  selector: 'sf-currency-update',
  templateUrl: './currency-update.component.html'
})
export class CurrencyUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)]],
    name: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(64)]],
    isDefault: [null, [Validators.required]],
    jsonval: [null, [Validators.required]]
  });

  constructor(protected currencyService: CurrencyService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ currency }) => {
      this.updateForm(currency);
    });
  }

  updateForm(currency: ICurrency) {
    this.editForm.patchValue({
      id: currency.id,
      code: currency.code,
      name: currency.name,
      isDefault: currency.isDefault,
      jsonval: currency.jsonval
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const currency = this.createFromForm();
    if (currency.id !== undefined) {
      this.subscribeToSaveResponse(this.currencyService.update(currency));
    } else {
      this.subscribeToSaveResponse(this.currencyService.create(currency));
    }
  }

  private createFromForm(): ICurrency {
    return {
      ...new Currency(),
      id: this.editForm.get(['id']).value,
      code: this.editForm.get(['code']).value,
      name: this.editForm.get(['name']).value,
      isDefault: this.editForm.get(['isDefault']).value,
      jsonval: this.editForm.get(['jsonval']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICurrency>>) {
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

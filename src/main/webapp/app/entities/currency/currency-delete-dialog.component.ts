import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from './currency.service';

@Component({
  selector: 'sf-currency-delete-dialog',
  templateUrl: './currency-delete-dialog.component.html'
})
export class CurrencyDeleteDialogComponent {
  currency: ICurrency;

  constructor(protected currencyService: CurrencyService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.currencyService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'currencyListModification',
        content: 'Deleted an currency'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'sf-currency-delete-popup',
  template: ''
})
export class CurrencyDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ currency }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(CurrencyDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.currency = currency;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/currency', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/currency', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}

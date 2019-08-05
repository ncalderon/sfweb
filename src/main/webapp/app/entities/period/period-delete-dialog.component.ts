import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPeriod } from 'app/shared/model/period.model';
import { PeriodService } from './period.service';

@Component({
  selector: 'sf-period-delete-dialog',
  templateUrl: './period-delete-dialog.component.html'
})
export class PeriodDeleteDialogComponent {
  period: IPeriod;

  constructor(protected periodService: PeriodService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.periodService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'periodListModification',
        content: 'Deleted an period'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'sf-period-delete-popup',
  template: ''
})
export class PeriodDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ period }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PeriodDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.period = period;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/period', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/period', { outlets: { popup: null } }]);
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

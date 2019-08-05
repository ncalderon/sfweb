import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFinAcc } from 'app/shared/model/fin-acc.model';
import { FinAccService } from './fin-acc.service';

@Component({
  selector: 'sf-fin-acc-delete-dialog',
  templateUrl: './fin-acc-delete-dialog.component.html'
})
export class FinAccDeleteDialogComponent {
  finAcc: IFinAcc;

  constructor(protected finAccService: FinAccService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.finAccService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'finAccListModification',
        content: 'Deleted an finAcc'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'sf-fin-acc-delete-popup',
  template: ''
})
export class FinAccDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ finAcc }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FinAccDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.finAcc = finAcc;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/fin-acc', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/fin-acc', { outlets: { popup: null } }]);
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

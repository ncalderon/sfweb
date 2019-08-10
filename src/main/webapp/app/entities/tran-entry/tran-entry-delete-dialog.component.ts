import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITranEntry } from 'app/shared/model/tran-entry.model';
import { TranEntryService } from './tran-entry.service';

@Component({
  selector: 'sf-tran-entry-delete-dialog',
  templateUrl: './tran-entry-delete-dialog.component.html'
})
export class TranEntryDeleteDialogComponent {
  tranEntry: ITranEntry;

  constructor(protected tranEntryService: TranEntryService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.tranEntryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'tranEntryListModification',
        content: 'Deleted an tranEntry'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'sf-tran-entry-delete-popup',
  template: ''
})
export class TranEntryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tranEntry }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TranEntryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.tranEntry = tranEntry;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/tran-entry', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/tran-entry', { outlets: { popup: null } }]);
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

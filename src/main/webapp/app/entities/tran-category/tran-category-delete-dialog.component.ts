import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITranCategory } from 'app/shared/model/tran-category.model';
import { TranCategoryService } from './tran-category.service';

@Component({
  selector: 'sf-tran-category-delete-dialog',
  templateUrl: './tran-category-delete-dialog.component.html'
})
export class TranCategoryDeleteDialogComponent {
  tranCategory: ITranCategory;

  constructor(
    protected tranCategoryService: TranCategoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.tranCategoryService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'tranCategoryListModification',
        content: 'Deleted an tranCategory'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'sf-tran-category-delete-popup',
  template: ''
})
export class TranCategoryDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ tranCategory }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TranCategoryDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.tranCategory = tranCategory;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/tran-category', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/tran-category', { outlets: { popup: null } }]);
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

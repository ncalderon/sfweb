import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUserPreference } from 'app/shared/model/user-preference.model';
import { UserPreferenceService } from './user-preference.service';

@Component({
  selector: 'sf-user-preference-delete-dialog',
  templateUrl: './user-preference-delete-dialog.component.html'
})
export class UserPreferenceDeleteDialogComponent {
  userPreference: IUserPreference;

  constructor(
    protected userPreferenceService: UserPreferenceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.userPreferenceService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'userPreferenceListModification',
        content: 'Deleted an userPreference'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'sf-user-preference-delete-popup',
  template: ''
})
export class UserPreferenceDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userPreference }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(UserPreferenceDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.userPreference = userPreference;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/user-preference', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/user-preference', { outlets: { popup: null } }]);
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

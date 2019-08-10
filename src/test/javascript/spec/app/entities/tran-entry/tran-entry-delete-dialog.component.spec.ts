/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SfwebTestModule } from '../../../test.module';
import { TranEntryDeleteDialogComponent } from 'app/entities/tran-entry/tran-entry-delete-dialog.component';
import { TranEntryService } from 'app/entities/tran-entry/tran-entry.service';

describe('Component Tests', () => {
  describe('TranEntry Management Delete Component', () => {
    let comp: TranEntryDeleteDialogComponent;
    let fixture: ComponentFixture<TranEntryDeleteDialogComponent>;
    let service: TranEntryService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SfwebTestModule],
        declarations: [TranEntryDeleteDialogComponent]
      })
        .overrideTemplate(TranEntryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TranEntryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TranEntryService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});

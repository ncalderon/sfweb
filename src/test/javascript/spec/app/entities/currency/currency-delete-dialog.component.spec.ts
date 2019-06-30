/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SfwebTestModule } from '../../../test.module';
import { CurrencyDeleteDialogComponent } from 'app/entities/currency/currency-delete-dialog.component';
import { CurrencyService } from 'app/entities/currency/currency.service';

describe('Component Tests', () => {
  describe('Currency Management Delete Component', () => {
    let comp: CurrencyDeleteDialogComponent;
    let fixture: ComponentFixture<CurrencyDeleteDialogComponent>;
    let service: CurrencyService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SfwebTestModule],
        declarations: [CurrencyDeleteDialogComponent]
      })
        .overrideTemplate(CurrencyDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CurrencyDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CurrencyService);
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

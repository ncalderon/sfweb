/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { SfwebTestModule } from '../../../test.module';
import { TranEntryUpdateComponent } from 'app/entities/tran-entry/tran-entry-update.component';
import { TranEntryService } from 'app/entities/tran-entry/tran-entry.service';
import { TranEntry } from 'app/shared/model/tran-entry.model';

describe('Component Tests', () => {
  describe('TranEntry Management Update Component', () => {
    let comp: TranEntryUpdateComponent;
    let fixture: ComponentFixture<TranEntryUpdateComponent>;
    let service: TranEntryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SfwebTestModule],
        declarations: [TranEntryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TranEntryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TranEntryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TranEntryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TranEntry(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TranEntry();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

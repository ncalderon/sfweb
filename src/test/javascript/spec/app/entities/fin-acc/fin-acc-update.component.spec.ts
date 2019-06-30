/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { SfwebTestModule } from '../../../test.module';
import { FinAccUpdateComponent } from 'app/entities/fin-acc/fin-acc-update.component';
import { FinAccService } from 'app/entities/fin-acc/fin-acc.service';
import { FinAcc } from 'app/shared/model/fin-acc.model';

describe('Component Tests', () => {
  describe('FinAcc Management Update Component', () => {
    let comp: FinAccUpdateComponent;
    let fixture: ComponentFixture<FinAccUpdateComponent>;
    let service: FinAccService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SfwebTestModule],
        declarations: [FinAccUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FinAccUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FinAccUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FinAccService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FinAcc(123);
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
        const entity = new FinAcc();
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

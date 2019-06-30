/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { SfwebTestModule } from '../../../test.module';
import { TranCategoryUpdateComponent } from 'app/entities/tran-category/tran-category-update.component';
import { TranCategoryService } from 'app/entities/tran-category/tran-category.service';
import { TranCategory } from 'app/shared/model/tran-category.model';

describe('Component Tests', () => {
  describe('TranCategory Management Update Component', () => {
    let comp: TranCategoryUpdateComponent;
    let fixture: ComponentFixture<TranCategoryUpdateComponent>;
    let service: TranCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SfwebTestModule],
        declarations: [TranCategoryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TranCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TranCategoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TranCategoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TranCategory(123);
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
        const entity = new TranCategory();
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

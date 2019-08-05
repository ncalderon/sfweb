/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { SfwebTestModule } from '../../../test.module';
import { UserPreferenceUpdateComponent } from 'app/entities/user-preference/user-preference-update.component';
import { UserPreferenceService } from 'app/entities/user-preference/user-preference.service';
import { UserPreference } from 'app/shared/model/user-preference.model';

describe('Component Tests', () => {
  describe('UserPreference Management Update Component', () => {
    let comp: UserPreferenceUpdateComponent;
    let fixture: ComponentFixture<UserPreferenceUpdateComponent>;
    let service: UserPreferenceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SfwebTestModule],
        declarations: [UserPreferenceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UserPreferenceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserPreferenceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserPreferenceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserPreference(123);
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
        const entity = new UserPreference();
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

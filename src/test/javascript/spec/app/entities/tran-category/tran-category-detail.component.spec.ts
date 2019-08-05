/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SfwebTestModule } from '../../../test.module';
import { TranCategoryDetailComponent } from 'app/entities/tran-category/tran-category-detail.component';
import { TranCategory } from 'app/shared/model/tran-category.model';

describe('Component Tests', () => {
  describe('TranCategory Management Detail Component', () => {
    let comp: TranCategoryDetailComponent;
    let fixture: ComponentFixture<TranCategoryDetailComponent>;
    const route = ({ data: of({ tranCategory: new TranCategory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SfwebTestModule],
        declarations: [TranCategoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TranCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TranCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tranCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

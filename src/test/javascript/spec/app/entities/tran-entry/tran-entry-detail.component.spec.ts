/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SfwebTestModule } from '../../../test.module';
import { TranEntryDetailComponent } from 'app/entities/tran-entry/tran-entry-detail.component';
import { TranEntry } from 'app/shared/model/tran-entry.model';

describe('Component Tests', () => {
  describe('TranEntry Management Detail Component', () => {
    let comp: TranEntryDetailComponent;
    let fixture: ComponentFixture<TranEntryDetailComponent>;
    const route = ({ data: of({ tranEntry: new TranEntry(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SfwebTestModule],
        declarations: [TranEntryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TranEntryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TranEntryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tranEntry).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

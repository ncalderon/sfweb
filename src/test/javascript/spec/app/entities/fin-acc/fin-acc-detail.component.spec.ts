/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SfwebTestModule } from '../../../test.module';
import { FinAccDetailComponent } from 'app/entities/fin-acc/fin-acc-detail.component';
import { FinAcc } from 'app/shared/model/fin-acc.model';

describe('Component Tests', () => {
  describe('FinAcc Management Detail Component', () => {
    let comp: FinAccDetailComponent;
    let fixture: ComponentFixture<FinAccDetailComponent>;
    const route = ({ data: of({ finAcc: new FinAcc(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SfwebTestModule],
        declarations: [FinAccDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FinAccDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FinAccDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.finAcc).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

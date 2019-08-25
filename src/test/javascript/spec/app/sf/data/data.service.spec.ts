import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { take } from 'rxjs/operators';
import { DataService } from 'app/sf/data';
import { TranFileType } from 'app/shared/model/data.model';

describe('Service Tests', () => {
  fdescribe('Data Service', () => {
    let injector: TestBed;
    let service: DataService;
    let httpMock: HttpTestingController;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(DataService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();
    });

    describe('Service methods', () => {
      it('should import transactions', async () => {
        let lines = [
          'tran_status;tran_type;tran_num;ref_num;post_date;description;amount;ccy_val;payment_method',
          'CLEARED;EXPENSE;0000001;0000001;2019-08-04;Transaction 1;1000;50;CASH',
          'CLEARED;EXPENSE;0000002;0000002;2019-08-05;Transaction 2;2000;50;CREDIT_CARD',
          'CLEARED;INCOME;0000003;0000003;2019-08-06;Transaction 3;3000;100;ELECTRONIC_TRANSFER'
        ];
        const tranFile = new File(lines, '');
        service
          .importTransactions(1, TranFileType.DEFAULT, tranFile)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.ok));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush({ status: 200 });
        expect(expectedResult);
        //expect(expectedResult).toMatchObject({ body: expected });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

/* tslint:disable max-line-length */
import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { map, take } from 'rxjs/operators';
import { FinAccService } from 'app/entities/fin-acc/fin-acc.service';
import { FinAcc, FinAccStatus, IFinAcc } from 'app/shared/model/fin-acc.model';

describe('Service Tests', () => {
  describe('FinAcc Service', () => {
    let injector: TestBed;
    let service: FinAccService;
    let httpMock: HttpTestingController;
    let elemDefault: IFinAcc;
    let expectedResult;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(FinAccService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new FinAcc(0, FinAccStatus.INACTIVE, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, false, 0, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a FinAcc', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new FinAcc(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a FinAcc', async () => {
        const returnedFromService = Object.assign(
          {
            status: 'BBBBBB',
            accNum: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            balance: 1,
            creditCard: true,
            billingCycle: 1,
            ccyCode: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of FinAcc', async () => {
        const returnedFromService = Object.assign(
          {
            status: 'BBBBBB',
            accNum: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            balance: 1,
            creditCard: true,
            billingCycle: 1,
            ccyCode: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FinAcc', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

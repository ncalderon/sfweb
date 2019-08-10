/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PeriodComponentsPage, PeriodDeleteDialog, PeriodUpdatePage } from './period.page-object';

const expect = chai.expect;

describe('Period e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let periodUpdatePage: PeriodUpdatePage;
  let periodComponentsPage: PeriodComponentsPage;
  let periodDeleteDialog: PeriodDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Periods', async () => {
    await navBarPage.goToEntity('period');
    periodComponentsPage = new PeriodComponentsPage();
    await browser.wait(ec.visibilityOf(periodComponentsPage.title), 5000);
    expect(await periodComponentsPage.getTitle()).to.eq('sfwebApp.period.home.title');
  });

  it('should load create Period page', async () => {
    await periodComponentsPage.clickOnCreateButton();
    periodUpdatePage = new PeriodUpdatePage();
    expect(await periodUpdatePage.getPageTitle()).to.eq('sfwebApp.period.home.createOrEditLabel');
    await periodUpdatePage.cancel();
  });

  it('should create and save Periods', async () => {
    const nbButtonsBeforeCreate = await periodComponentsPage.countDeleteButtons();

    await periodComponentsPage.clickOnCreateButton();
    await promise.all([periodUpdatePage.setMonthInput('5')]);
    expect(await periodUpdatePage.getMonthInput()).to.eq('5', 'Expected month value to be equals to 5');
    await periodUpdatePage.save();
    expect(await periodUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await periodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Period', async () => {
    const nbButtonsBeforeDelete = await periodComponentsPage.countDeleteButtons();
    await periodComponentsPage.clickOnLastDeleteButton();

    periodDeleteDialog = new PeriodDeleteDialog();
    expect(await periodDeleteDialog.getDialogTitle()).to.eq('sfwebApp.period.delete.question');
    await periodDeleteDialog.clickOnConfirmButton();

    expect(await periodComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

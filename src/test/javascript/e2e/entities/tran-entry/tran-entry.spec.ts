/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { TranEntryComponentsPage, TranEntryDeleteDialog, TranEntryUpdatePage } from './tran-entry.page-object';

const expect = chai.expect;

describe('TranEntry e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tranEntryUpdatePage: TranEntryUpdatePage;
  let tranEntryComponentsPage: TranEntryComponentsPage;
  /*let tranEntryDeleteDialog: TranEntryDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load TranEntries', async () => {
    await navBarPage.goToEntity('tran-entry');
    tranEntryComponentsPage = new TranEntryComponentsPage();
    await browser.wait(ec.visibilityOf(tranEntryComponentsPage.title), 5000);
    expect(await tranEntryComponentsPage.getTitle()).to.eq('sfwebApp.tranEntry.home.title');
  });

  it('should load create TranEntry page', async () => {
    await tranEntryComponentsPage.clickOnCreateButton();
    tranEntryUpdatePage = new TranEntryUpdatePage();
    expect(await tranEntryUpdatePage.getPageTitle()).to.eq('sfwebApp.tranEntry.home.createOrEditLabel');
    await tranEntryUpdatePage.cancel();
  });

  /* it('should create and save TranEntries', async () => {
        const nbButtonsBeforeCreate = await tranEntryComponentsPage.countDeleteButtons();

        await tranEntryComponentsPage.clickOnCreateButton();
        await promise.all([
            tranEntryUpdatePage.tranStatusSelectLastOption(),
            tranEntryUpdatePage.tranTypeSelectLastOption(),
            tranEntryUpdatePage.setTranNumInput('tranNum'),
            tranEntryUpdatePage.setRefNumInput('refNum'),
            tranEntryUpdatePage.setPostDateInput('2000-12-31'),
            tranEntryUpdatePage.setDescriptionInput('description'),
            tranEntryUpdatePage.setAmountInput('5'),
            tranEntryUpdatePage.setCcyValInput('5'),
            tranEntryUpdatePage.paymentMethodSelectLastOption(),
            tranEntryUpdatePage.finAccSelectLastOption(),
            tranEntryUpdatePage.tranCategorySelectLastOption(),
        ]);
        expect(await tranEntryUpdatePage.getTranNumInput()).to.eq('tranNum', 'Expected TranNum value to be equals to tranNum');
        expect(await tranEntryUpdatePage.getRefNumInput()).to.eq('refNum', 'Expected RefNum value to be equals to refNum');
        expect(await tranEntryUpdatePage.getPostDateInput()).to.eq('2000-12-31', 'Expected postDate value to be equals to 2000-12-31');
        expect(await tranEntryUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
        expect(await tranEntryUpdatePage.getAmountInput()).to.eq('5', 'Expected amount value to be equals to 5');
        expect(await tranEntryUpdatePage.getCcyValInput()).to.eq('5', 'Expected ccyVal value to be equals to 5');
        await tranEntryUpdatePage.save();
        expect(await tranEntryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await tranEntryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    });*/

  /* it('should delete last TranEntry', async () => {
        const nbButtonsBeforeDelete = await tranEntryComponentsPage.countDeleteButtons();
        await tranEntryComponentsPage.clickOnLastDeleteButton();

        tranEntryDeleteDialog = new TranEntryDeleteDialog();
        expect(await tranEntryDeleteDialog.getDialogTitle())
            .to.eq('sfwebApp.tranEntry.delete.question');
        await tranEntryDeleteDialog.clickOnConfirmButton();

        expect(await tranEntryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

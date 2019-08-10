/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { FinAccComponentsPage, FinAccUpdatePage } from './fin-acc.page-object';

const expect = chai.expect;

describe('FinAcc e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let finAccUpdatePage: FinAccUpdatePage;
  let finAccComponentsPage: FinAccComponentsPage;
  /*let finAccDeleteDialog: FinAccDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load FinAccs', async () => {
    await navBarPage.goToEntity('fin-acc');
    finAccComponentsPage = new FinAccComponentsPage();
    await browser.wait(ec.visibilityOf(finAccComponentsPage.title), 5000);
    expect(await finAccComponentsPage.getTitle()).to.eq('sfwebApp.finAcc.home.title');
  });

  it('should load create FinAcc page', async () => {
    await finAccComponentsPage.clickOnCreateButton();
    finAccUpdatePage = new FinAccUpdatePage();
    expect(await finAccUpdatePage.getPageTitle()).to.eq('sfwebApp.finAcc.home.createOrEditLabel');
    await finAccUpdatePage.cancel();
  });

  /* it('should create and save FinAccs', async () => {
        const nbButtonsBeforeCreate = await finAccComponentsPage.countDeleteButtons();

        await finAccComponentsPage.clickOnCreateButton();
        await promise.all([
            finAccUpdatePage.statusSelectLastOption(),
            finAccUpdatePage.setAccNumInput('accNum'),
            finAccUpdatePage.setNameInput('name'),
            finAccUpdatePage.setDescriptionInput('description'),
            finAccUpdatePage.setBalanceInput('5'),
            finAccUpdatePage.setBillingCycleInput('5'),
            finAccUpdatePage.setCcyCodeInput('ccyCode'),
            finAccUpdatePage.userSelectLastOption(),
        ]);
        expect(await finAccUpdatePage.getAccNumInput()).to.eq('accNum', 'Expected AccNum value to be equals to accNum');
        expect(await finAccUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
        expect(await finAccUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
        expect(await finAccUpdatePage.getBalanceInput()).to.eq('5', 'Expected balance value to be equals to 5');
        const selectedCreditCard = finAccUpdatePage.getCreditCardInput();
        if (await selectedCreditCard.isSelected()) {
            await finAccUpdatePage.getCreditCardInput().click();
            expect(await finAccUpdatePage.getCreditCardInput().isSelected(), 'Expected creditCard not to be selected').to.be.false;
        } else {
            await finAccUpdatePage.getCreditCardInput().click();
            expect(await finAccUpdatePage.getCreditCardInput().isSelected(), 'Expected creditCard to be selected').to.be.true;
        }
        expect(await finAccUpdatePage.getBillingCycleInput()).to.eq('5', 'Expected billingCycle value to be equals to 5');
        expect(await finAccUpdatePage.getCcyCodeInput()).to.eq('ccyCode', 'Expected CcyCode value to be equals to ccyCode');
        await finAccUpdatePage.save();
        expect(await finAccUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await finAccComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    });*/

  /* it('should delete last FinAcc', async () => {
        const nbButtonsBeforeDelete = await finAccComponentsPage.countDeleteButtons();
        await finAccComponentsPage.clickOnLastDeleteButton();

        finAccDeleteDialog = new FinAccDeleteDialog();
        expect(await finAccDeleteDialog.getDialogTitle())
            .to.eq('sfwebApp.finAcc.delete.question');
        await finAccDeleteDialog.clickOnConfirmButton();

        expect(await finAccComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

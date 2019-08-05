/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BudgetComponentsPage, BudgetDeleteDialog, BudgetUpdatePage } from './budget.page-object';

const expect = chai.expect;

describe('Budget e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let budgetUpdatePage: BudgetUpdatePage;
  let budgetComponentsPage: BudgetComponentsPage;
  /*let budgetDeleteDialog: BudgetDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Budgets', async () => {
    await navBarPage.goToEntity('budget');
    budgetComponentsPage = new BudgetComponentsPage();
    await browser.wait(ec.visibilityOf(budgetComponentsPage.title), 5000);
    expect(await budgetComponentsPage.getTitle()).to.eq('sfwebApp.budget.home.title');
  });

  it('should load create Budget page', async () => {
    await budgetComponentsPage.clickOnCreateButton();
    budgetUpdatePage = new BudgetUpdatePage();
    expect(await budgetUpdatePage.getPageTitle()).to.eq('sfwebApp.budget.home.createOrEditLabel');
    await budgetUpdatePage.cancel();
  });

  /* it('should create and save Budgets', async () => {
        const nbButtonsBeforeCreate = await budgetComponentsPage.countDeleteButtons();

        await budgetComponentsPage.clickOnCreateButton();
        await promise.all([
            budgetUpdatePage.setAmountInput('5'),
            budgetUpdatePage.tranCategorySelectLastOption(),
            budgetUpdatePage.periodSelectLastOption(),
        ]);
        expect(await budgetUpdatePage.getAmountInput()).to.eq('5', 'Expected amount value to be equals to 5');
        await budgetUpdatePage.save();
        expect(await budgetUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await budgetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    });*/

  /* it('should delete last Budget', async () => {
        const nbButtonsBeforeDelete = await budgetComponentsPage.countDeleteButtons();
        await budgetComponentsPage.clickOnLastDeleteButton();

        budgetDeleteDialog = new BudgetDeleteDialog();
        expect(await budgetDeleteDialog.getDialogTitle())
            .to.eq('sfwebApp.budget.delete.question');
        await budgetDeleteDialog.clickOnConfirmButton();

        expect(await budgetComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

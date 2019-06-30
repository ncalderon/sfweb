/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { UserPreferenceComponentsPage, UserPreferenceDeleteDialog, UserPreferenceUpdatePage } from './user-preference.page-object';

const expect = chai.expect;

describe('UserPreference e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let userPreferenceUpdatePage: UserPreferenceUpdatePage;
  let userPreferenceComponentsPage: UserPreferenceComponentsPage;
  /*let userPreferenceDeleteDialog: UserPreferenceDeleteDialog;*/

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load UserPreferences', async () => {
    await navBarPage.goToEntity('user-preference');
    userPreferenceComponentsPage = new UserPreferenceComponentsPage();
    await browser.wait(ec.visibilityOf(userPreferenceComponentsPage.title), 5000);
    expect(await userPreferenceComponentsPage.getTitle()).to.eq('sfwebApp.userPreference.home.title');
  });

  it('should load create UserPreference page', async () => {
    await userPreferenceComponentsPage.clickOnCreateButton();
    userPreferenceUpdatePage = new UserPreferenceUpdatePage();
    expect(await userPreferenceUpdatePage.getPageTitle()).to.eq('sfwebApp.userPreference.home.createOrEditLabel');
    await userPreferenceUpdatePage.cancel();
  });

  /* it('should create and save UserPreferences', async () => {
        const nbButtonsBeforeCreate = await userPreferenceComponentsPage.countDeleteButtons();

        await userPreferenceComponentsPage.clickOnCreateButton();
        await promise.all([
            userPreferenceUpdatePage.setValueInput('value'),
            userPreferenceUpdatePage.userSelectLastOption(),
            userPreferenceUpdatePage.preferenceSelectLastOption(),
        ]);
        expect(await userPreferenceUpdatePage.getValueInput()).to.eq('value', 'Expected Value value to be equals to value');
        await userPreferenceUpdatePage.save();
        expect(await userPreferenceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await userPreferenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    });*/

  /* it('should delete last UserPreference', async () => {
        const nbButtonsBeforeDelete = await userPreferenceComponentsPage.countDeleteButtons();
        await userPreferenceComponentsPage.clickOnLastDeleteButton();

        userPreferenceDeleteDialog = new UserPreferenceDeleteDialog();
        expect(await userPreferenceDeleteDialog.getDialogTitle())
            .to.eq('sfwebApp.userPreference.delete.question');
        await userPreferenceDeleteDialog.clickOnConfirmButton();

        expect(await userPreferenceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

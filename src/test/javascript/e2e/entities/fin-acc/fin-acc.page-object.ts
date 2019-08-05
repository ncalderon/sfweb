import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class FinAccComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('sf-fin-acc div table .btn-danger'));
  title = element.all(by.css('sf-fin-acc div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class FinAccUpdatePage {
  pageTitle = element(by.id('sf-fin-acc-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  statusSelect = element(by.id('field_status'));
  accNumInput = element(by.id('field_accNum'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  balanceInput = element(by.id('field_balance'));
  isCreditCardInput = element(by.id('field_isCreditCard'));
  billingCycleInput = element(by.id('field_billingCycle'));
  ccyCodeInput = element(by.id('field_ccyCode'));
  userSelect = element(by.id('field_user'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setStatusSelect(status) {
    await this.statusSelect.sendKeys(status);
  }

  async getStatusSelect() {
    return await this.statusSelect.element(by.css('option:checked')).getText();
  }

  async statusSelectLastOption(timeout?: number) {
    await this.statusSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setAccNumInput(accNum) {
    await this.accNumInput.sendKeys(accNum);
  }

  async getAccNumInput() {
    return await this.accNumInput.getAttribute('value');
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return await this.nameInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return await this.descriptionInput.getAttribute('value');
  }

  async setBalanceInput(balance) {
    await this.balanceInput.sendKeys(balance);
  }

  async getBalanceInput() {
    return await this.balanceInput.getAttribute('value');
  }

  getIsCreditCardInput(timeout?: number) {
    return this.isCreditCardInput;
  }
  async setBillingCycleInput(billingCycle) {
    await this.billingCycleInput.sendKeys(billingCycle);
  }

  async getBillingCycleInput() {
    return await this.billingCycleInput.getAttribute('value');
  }

  async setCcyCodeInput(ccyCode) {
    await this.ccyCodeInput.sendKeys(ccyCode);
  }

  async getCcyCodeInput() {
    return await this.ccyCodeInput.getAttribute('value');
  }

  async userSelectLastOption(timeout?: number) {
    await this.userSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userSelectOption(option) {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption() {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class FinAccDeleteDialog {
  private dialogTitle = element(by.id('sf-delete-finAcc-heading'));
  private confirmButton = element(by.id('sf-confirm-delete-finAcc'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}

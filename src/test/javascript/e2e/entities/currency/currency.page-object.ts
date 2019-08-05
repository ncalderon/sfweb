import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class CurrencyComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('sf-currency div table .btn-danger'));
  title = element.all(by.css('sf-currency div h2#page-heading span')).first();

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

export class CurrencyUpdatePage {
  pageTitle = element(by.id('sf-currency-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  codeInput = element(by.id('field_code'));
  nameInput = element(by.id('field_name'));
  isDefaultInput = element(by.id('field_isDefault'));
  jsonvalInput = element(by.id('field_jsonval'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setCodeInput(code) {
    await this.codeInput.sendKeys(code);
  }

  async getCodeInput() {
    return await this.codeInput.getAttribute('value');
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return await this.nameInput.getAttribute('value');
  }

  getIsDefaultInput(timeout?: number) {
    return this.isDefaultInput;
  }
  async setJsonvalInput(jsonval) {
    await this.jsonvalInput.sendKeys(jsonval);
  }

  async getJsonvalInput() {
    return await this.jsonvalInput.getAttribute('value');
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

export class CurrencyDeleteDialog {
  private dialogTitle = element(by.id('sf-delete-currency-heading'));
  private confirmButton = element(by.id('sf-confirm-delete-currency'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}

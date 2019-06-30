import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class UserPreferenceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('sf-user-preference div table .btn-danger'));
  title = element.all(by.css('sf-user-preference div h2#page-heading span')).first();

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

export class UserPreferenceUpdatePage {
  pageTitle = element(by.id('sf-user-preference-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  valueInput = element(by.id('field_value'));
  userSelect = element(by.id('field_user'));
  preferenceSelect = element(by.id('field_preference'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setValueInput(value) {
    await this.valueInput.sendKeys(value);
  }

  async getValueInput() {
    return await this.valueInput.getAttribute('value');
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

  async preferenceSelectLastOption(timeout?: number) {
    await this.preferenceSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async preferenceSelectOption(option) {
    await this.preferenceSelect.sendKeys(option);
  }

  getPreferenceSelect(): ElementFinder {
    return this.preferenceSelect;
  }

  async getPreferenceSelectedOption() {
    return await this.preferenceSelect.element(by.css('option:checked')).getText();
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

export class UserPreferenceDeleteDialog {
  private dialogTitle = element(by.id('sf-delete-userPreference-heading'));
  private confirmButton = element(by.id('sf-confirm-delete-userPreference'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}

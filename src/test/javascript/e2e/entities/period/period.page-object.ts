import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class PeriodComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('sf-period div table .btn-danger'));
  title = element.all(by.css('sf-period div h2#page-heading span')).first();

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

export class PeriodUpdatePage {
  pageTitle = element(by.id('sf-period-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  monthInput = element(by.id('field_month'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setMonthInput(month) {
    await this.monthInput.sendKeys(month);
  }

  async getMonthInput() {
    return await this.monthInput.getAttribute('value');
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

export class PeriodDeleteDialog {
  private dialogTitle = element(by.id('sf-delete-period-heading'));
  private confirmButton = element(by.id('sf-confirm-delete-period'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}

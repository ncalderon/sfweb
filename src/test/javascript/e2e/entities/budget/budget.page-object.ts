import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class BudgetComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('sf-budget div table .btn-danger'));
  title = element.all(by.css('sf-budget div h2#page-heading span')).first();

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

export class BudgetUpdatePage {
  pageTitle = element(by.id('sf-budget-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  amountInput = element(by.id('field_amount'));
  tranCategorySelect = element(by.id('field_tranCategory'));
  periodSelect = element(by.id('field_period'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setAmountInput(amount) {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput() {
    return await this.amountInput.getAttribute('value');
  }

  async tranCategorySelectLastOption(timeout?: number) {
    await this.tranCategorySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async tranCategorySelectOption(option) {
    await this.tranCategorySelect.sendKeys(option);
  }

  getTranCategorySelect(): ElementFinder {
    return this.tranCategorySelect;
  }

  async getTranCategorySelectedOption() {
    return await this.tranCategorySelect.element(by.css('option:checked')).getText();
  }

  async periodSelectLastOption(timeout?: number) {
    await this.periodSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async periodSelectOption(option) {
    await this.periodSelect.sendKeys(option);
  }

  getPeriodSelect(): ElementFinder {
    return this.periodSelect;
  }

  async getPeriodSelectedOption() {
    return await this.periodSelect.element(by.css('option:checked')).getText();
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

export class BudgetDeleteDialog {
  private dialogTitle = element(by.id('sf-delete-budget-heading'));
  private confirmButton = element(by.id('sf-confirm-delete-budget'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}

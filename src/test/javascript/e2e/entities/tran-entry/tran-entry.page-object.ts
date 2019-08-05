import { browser, ExpectedConditions, element, by, ElementFinder } from 'protractor';

export class TranEntryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('sf-tran-entry div table .btn-danger'));
  title = element.all(by.css('sf-tran-entry div h2#page-heading span')).first();

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

export class TranEntryUpdatePage {
  pageTitle = element(by.id('sf-tran-entry-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  tranStatusSelect = element(by.id('field_tranStatus'));
  tranTypeSelect = element(by.id('field_tranType'));
  tranNumInput = element(by.id('field_tranNum'));
  refNumInput = element(by.id('field_refNum'));
  postDateInput = element(by.id('field_postDate'));
  descriptionInput = element(by.id('field_description'));
  amountInput = element(by.id('field_amount'));
  ccyValInput = element(by.id('field_ccyVal'));
  paymentMethodSelect = element(by.id('field_paymentMethod'));
  finAccSelect = element(by.id('field_finAcc'));
  tranCategorySelect = element(by.id('field_tranCategory'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setTranStatusSelect(tranStatus) {
    await this.tranStatusSelect.sendKeys(tranStatus);
  }

  async getTranStatusSelect() {
    return await this.tranStatusSelect.element(by.css('option:checked')).getText();
  }

  async tranStatusSelectLastOption(timeout?: number) {
    await this.tranStatusSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setTranTypeSelect(tranType) {
    await this.tranTypeSelect.sendKeys(tranType);
  }

  async getTranTypeSelect() {
    return await this.tranTypeSelect.element(by.css('option:checked')).getText();
  }

  async tranTypeSelectLastOption(timeout?: number) {
    await this.tranTypeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async setTranNumInput(tranNum) {
    await this.tranNumInput.sendKeys(tranNum);
  }

  async getTranNumInput() {
    return await this.tranNumInput.getAttribute('value');
  }

  async setRefNumInput(refNum) {
    await this.refNumInput.sendKeys(refNum);
  }

  async getRefNumInput() {
    return await this.refNumInput.getAttribute('value');
  }

  async setPostDateInput(postDate) {
    await this.postDateInput.sendKeys(postDate);
  }

  async getPostDateInput() {
    return await this.postDateInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return await this.descriptionInput.getAttribute('value');
  }

  async setAmountInput(amount) {
    await this.amountInput.sendKeys(amount);
  }

  async getAmountInput() {
    return await this.amountInput.getAttribute('value');
  }

  async setCcyValInput(ccyVal) {
    await this.ccyValInput.sendKeys(ccyVal);
  }

  async getCcyValInput() {
    return await this.ccyValInput.getAttribute('value');
  }

  async setPaymentMethodSelect(paymentMethod) {
    await this.paymentMethodSelect.sendKeys(paymentMethod);
  }

  async getPaymentMethodSelect() {
    return await this.paymentMethodSelect.element(by.css('option:checked')).getText();
  }

  async paymentMethodSelectLastOption(timeout?: number) {
    await this.paymentMethodSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async finAccSelectLastOption(timeout?: number) {
    await this.finAccSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async finAccSelectOption(option) {
    await this.finAccSelect.sendKeys(option);
  }

  getFinAccSelect(): ElementFinder {
    return this.finAccSelect;
  }

  async getFinAccSelectedOption() {
    return await this.finAccSelect.element(by.css('option:checked')).getText();
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

export class TranEntryDeleteDialog {
  private dialogTitle = element(by.id('sf-delete-tranEntry-heading'));
  private confirmButton = element(by.id('sf-confirm-delete-tranEntry'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}

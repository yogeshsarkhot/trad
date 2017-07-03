import { TradClientPage } from './app.po';

describe('trad-client App', () => {
  let page: TradClientPage;

  beforeEach(() => {
    page = new TradClientPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});

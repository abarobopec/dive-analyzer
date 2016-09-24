import { DiveAnalyzerPage } from './app.po';

describe('dive-analyzer App', function() {
  let page: DiveAnalyzerPage;

  beforeEach(() => {
    page = new DiveAnalyzerPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});

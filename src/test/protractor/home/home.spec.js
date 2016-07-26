/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeEach */

'use strict';
var htmlContentHelper =
require('../utils/htmlContentHelper');

describe('Home page', function() {
  function testHomePage(description, link) {
    describe(description, function() {
      var currentUrl;
      var htmlContent;

      beforeEach(function() {
          browser.get(link);
          browser.getCurrentUrl().then(function(url) {
           currentUrl = url;
         });
          browser.waitForAngular();
          browser.manage().timeouts().implicitlyWait(1000);
          htmlContent = element(by.id('content'));
        });
      it('should check translated strings', function() {
          htmlContentHelper
          .findNotTranslationedStrings(htmlContent, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
        });
      it('should check states', function() {
        // comming soon....
      });
    });
  }
  testHomePage('with german language', '#/de/');
  testHomePage('with english language', '#/en/');
});

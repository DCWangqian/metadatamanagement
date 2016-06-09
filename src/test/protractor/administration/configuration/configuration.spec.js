/* global browser */
/* global it */
/* global describe */
/* global by */
/* global element */
/* global expect */
/* global beforeAll */
/* global afterAll */

'use strict';

var htmlContentHelper =
require('../../utils/htmlContentHelper');
var loginHelper = require('../../utils/loginHelper');

describe('Configuration Page', function() {
  function testConfigurationPage(description, link) {
    describe(description, function() {
      var currentUrl;
      var htmlContent;
      beforeAll(function() {
          loginHelper.login();
          browser.get(link);
          browser.getCurrentUrl().then(function(url) {
           currentUrl = url;
         });
          htmlContent = element(by.id('content'));
        });
      afterAll(function() {
          loginHelper.logout();
        });
      it('should check translated strings', function() {
          htmlContentHelper
          .findNotTranslationedStrings(htmlContent, currentUrl)
          .then(function(result) {
            expect(result.length).toBe(0, result.message);
          });
        });
    });
  }
  testConfigurationPage('with german language', '#/de/configuration');
  testConfigurationPage('with english language', '#/en/configuration');
});

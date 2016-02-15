/* global describe */
/* global it */
/* global afterAll */
/* @Author Daniel Katzberg */

'use strict';

var utilMissingTranslations = require('../utils/findMissingTranslations');
var pages = ['/', '/login', '/register', '/reset/request',
  '/disclosure',
];
var cacheHelper = require('../utils/cacheHelper');

describe('Metadatamanagement Start page with different languages', function () {

  afterAll(cacheHelper.clearCache);

  it('Check german language elements for all pages', function (done) {
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.germanLanguage, pages, done);
  });

  it('Check english language elements for all pages', function (done) {
    utilMissingTranslations.testMissingTranslations(
      utilMissingTranslations.englishLanguage, pages, done);
  });
});

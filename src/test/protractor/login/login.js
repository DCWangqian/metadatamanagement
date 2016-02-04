/* global browser */
/* global it */
/* global describe */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

describe('Test login', function() {

  var loginHelper = require('../utils/loginHelper');

  it('Login in german with admin', function() {
    var languagePath = '#/de/';
    //Welcome Page
    browser.get(languagePath);
    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/de/');

    //'Login'
    loginHelper.login(languagePath);

    //'Logout'
    loginHelper.logout(languagePath);
  });

  it('Login in english with admin', function() {
    var languagePath = '#/en/';
    //Welcome Page
    browser.get(languagePath);
    expect(browser.getCurrentUrl()).toEqual(
      'https://metadatamanagement.cfapps.io/#/en/');

    //'Login'
    loginHelper.login(languagePath);

    //'Logout'
    loginHelper.logout(languagePath);
  });
});

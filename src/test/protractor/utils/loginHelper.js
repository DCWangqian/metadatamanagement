/* global element */
/* global by */
/* global expect */
/* @Author Daniel Katzberg */
'use strict';

function logout(languagePath, done) {
  //expect(element(by.dataTranslate('global.menu.entities.main'))).toBe(true);

  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click().then(done);

  //Click at Logout
  element(by.ngClick('logout()')).click().then(done);

  element.all(by.dataTranslate('global.menu.entities.main')).then(function (entities) {
    expect(entities.length).toBe(0); //four drop downs
  });
}

function login(languagePath, done) {

  //Expect no entities dropdown (only visible with a login)
  expect(element(by.dataTranslate('global.menu.entities.main')).isPresent()).toBe(false);

  //Click at 'Account'
  element(by.dataTranslate('global.menu.account.main')).click().then(done);

  //Check for login button
  element(by.uiSref('login')).isPresent().then(function (isPresent) {
    if (isPresent === false) {
      logout();
    }
  });

  //Click at 'Login'
  expect(element(by.uiSref('login')).isPresent()).toBe(true);
  element(by.uiSref('login')).click().then(done);

  //add account information and login
  element(by.id('username')).sendKeys('admin').then(done);
  element(by.id('password')).sendKeys('admin').then(done);
  element(by.dataTranslate('login.form.button')).click().then(done);

  element.all(by.dataTranslate('global.menu.entities.main')).then(function (entities) {
    expect(entities.length).toBe(1); //four drop downs
  });
}

module.exports.login = login;
module.exports.logout = logout;

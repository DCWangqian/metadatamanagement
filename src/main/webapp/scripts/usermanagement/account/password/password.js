'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('password', {
      parent: 'account',
      url: '/password',
      data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'global.menu.account.password'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/' +
            'password/password.html.tmpl',
          controller: 'PasswordController'
        }
      },
      resolve: {
        translatePartialLoader: ['$translate', '$translatePartialLoader',
          function($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('password');
            return $translate.refresh();
          }
        ]
      }
    });
  });

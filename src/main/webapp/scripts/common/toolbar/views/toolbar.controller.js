'use strict';

angular.module('metadatamanagementApp').controller(
  'ToolbarController',
  function($scope, $state, Auth, Principal, $mdSidenav, Language, $mdMedia) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //Set Languages
    $scope.changeLanguage = function(languageKey) {
      Language.setCurrent(languageKey);
    };

    $scope.$mdMedia = $mdMedia;

    //Goto Logout Page
    $scope.logout = function() {
      Auth.logout();
      $state.go('search');
    };

    //Goto Login Page
    $scope.login = function() {
      $state.go('login');
    };

    //Register function
    $scope.register = function() {
      $state.go('register');
    };

    //Toggle Function
    $scope.toggleLeft = function() {
      $mdSidenav('SideNavBar').toggle();
    };
  });

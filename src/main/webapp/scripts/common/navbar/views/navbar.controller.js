/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal, $mdSidenav, $document, $timeout,
    LanguageService, Auth, $state,
    FdzWelcomeDialogService) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
    $scope.isAccountMenuOpen = false;

    $scope.openDialog = function() {
      var openByNavbarFeedbackButton = true;
      FdzWelcomeDialogService.showDialog(openByNavbarFeedbackButton);
    };

    //Functions for toggling buttons.
    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

    $scope.close = function() {
      if (!$mdSidenav('SideNavBar').isLockedOpen()) {
        $timeout($mdSidenav('SideNavBar').toggle, 200);
      }
    };

    $scope.focusContent = function() {
      $document.find('.fdz-content')[0].focus();
    };

    //Set Languages
    $scope.changeLanguage = function(languageKey) {
      LanguageService.setCurrent(languageKey);
    };

    //Goto Logout Page
    $scope.logout = function() {
      Auth.logout();
      $state.go('search', {
        lang: LanguageService.getCurrentInstantly()
      });
      $scope.close();
    };

    $scope.$watch(function() {
      return {
        width: $document.find('#SideNavBar')[0].clientWidth,
        isLockedOpen: $mdSidenav('SideNavBar').isLockedOpen()
      };
    }, function(newVal) {
      if (newVal.isLockedOpen) {
        $document.find('#content-container').css(
          'margin-left', newVal.width);
      } else {
        $document.find('#content-container').css(
          'margin-left', 0);
      }
    }, true);
  });

'use strict';

angular.module('metadatamanagementApp').directive('fdzWelcomeDialog',
  function($mdDialog, localStorageService) {
    return {
      restrict: 'E',
      link: function($scope) {
          if (!localStorageService.get('closeDialogForEver')) {
            // jscs:disable
            $mdDialog.show({
              template: '<md-dialog aria-label="Submitting Bugs and Feedback" md-autofocus>' +
              '<md-toolbar>' +
              ' <div class="md-toolbar-tools" layout="row" layout-align="space-between center">' +
              '    <h2>{{"global.welcomeDialog.toolbar-head" |translate}}</h2>' +
              '     <md-button class="md-icon-button" ng-click="closeDialog()">' +
              '    <md-icon md-font-set="material-icons">clear</md-icon>' +
              '    <md-tooltip md-autohide="true" md-z-index="bowser.mobile || bowser.tablet ? -100 : 100001">' +
              '        {{"global.dialog.tooltip.close" | translate}}' +
              '    </md-tooltip>' +
              '</md-button>' +
              '  </div>' +
              '</md-toolbar>' +
              '   <div class="md-dialog-content" layout="column">' +
              '<strong>{{"global.welcomeDialog.content-head" | translate}}</strong>' +
              '<span>{{"global.welcomeDialog.content-body-with" | translate}}&nbsp;<a href="https://github.com/dzhw/metadatamanagement/issues/new" target="_blank">GitHub' +
              '  <md-tooltip md-autohide="true" md-z-index="bowser.mobile || bowser.tablet ? -100 : 100001">' +
              '      {{"global.tooltips.welcomeDialog.github" | translate}}' +
              '</md-tooltip>' +
              '</a>{{"global.welcomeDialog.content-body" | translate}}</span>' +
              '</div>' +
              '<md-dialog-actions>' +
              '   <md-button class="md-raised" ng-click="closeDialogForEver()">' +
              '       {{"global.buttons.closeDialogForEver" | translate}}' +
              '       <md-tooltip md-autohide="true" md-z-index="bowser.mobile || bowser.tablet ? -100 : 100001">' +
              '          {{"global.dialog.tooltip.closeDialogForEver" | translate}}' +
              '       </md-tooltip>' +
              '   </md-button>' +
              '   <md-button class="md-primary md-raised" id="closeWelcomeDialog" ng-click="closeDialog()">' +
              '       {{"global.buttons.closeDialogTemporarily" | translate}}' +
              '       <md-tooltip md-autohide="true" md-z-index="bowser.mobile || bowser.tablet ? -100 : 100001">' +
              '          {{"global.dialog.tooltip.close" | translate}}' +
              '       </md-tooltip>' +
              '    </md-button>' +
              '</md-dialog-actions>' +
              '</md-dialog>',
              controller: 'FdzWelcomeDialogController',
              locals : {
                bowser : $scope.bowser
              },
              clickOutsideToClose: true
            });
            // jscs:enable
          }
        },
      scope: {
          bowser: '='
        }
    };
  });

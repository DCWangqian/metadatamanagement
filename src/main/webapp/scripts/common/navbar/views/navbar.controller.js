/* Author Daniel Katzberg */
'use strict';

angular.module('metadatamanagementApp').controller('NavbarController',
  function($scope, Principal,
    CurrentProjectService, DataAcquisitionProjectPostValidationService,
    DataAcquisitionProjectSearchResource, DataAcquisitionProjectResource,
    $mdDialog, SimpleMessageToastService, $translate, CleanJSObjectService) {
    $scope.isAuthenticated = Principal.isAuthenticated;

    //For toggle buttons
    $scope.isProjectMenuOpen = false;
    $scope.isAdminMenuOpen = false;
    $scope.isAccountMenuOpen = false;

    //helper method
    function include(array, objectInArray) {
      return (array.indexOf(objectInArray) !== -1);
    }

    //For Project Handling
    $scope.dataAcquisitionProjects = null;
    $scope.project = null;
    $scope.selectedProject = null;
    $scope.disableAutocomplete = false;

    function checkEmptyListProjects() {
      if ($scope.dataAcquisitionProjects) {

        //It is possible that a project could be deleted after it is selected.
        //This method checks it, because it will be called for disabling the
        //the drop dpwn menu.
        if (!include($scope.dataAcquisitionProjects, $scope.project)) {
          //$scope.updateCurrentProject(null);
        }

        //Empty List -> Disable the Drop Down
        $scope.disableAutocomplete = ($scope.dataAcquisitionProjects.length ===
          0);
      }
    }

    //Set the current project again, if e.g. a language change happens and the
    //navbar will be rendered again
    function setCurrentProject() {
      if (!CleanJSObjectService.isNullOrEmpty(
          CurrentProjectService.getCurrentProject())) {
        $scope.selectedProject = CurrentProjectService.getCurrentProject();
        $scope.updateCurrentProject(CurrentProjectService.getCurrentProject());
      }
    }

    //Load the projects for the drop menu
    $scope.loadProjects = function() {
      DataAcquisitionProjectSearchResource.query({},
        function(result) {
          $scope.dataAcquisitionProjects =
            result._embedded.dataAcquisitionProjects;
          checkEmptyListProjects();
          setCurrentProject();
        });
    };
    $scope.loadProjects();

    //Helper method for query the project list
    function createFilterFor(query) {
      var lowercaseQuery = angular.lowercase(query);
      return function filterFn(project) {
        return (project.id.indexOf(lowercaseQuery) === 0);
      };
    }

    //Query for searching in project list
    $scope.querySearch = function(query) {
      var results = query ? $scope.dataAcquisitionProjects.filter(
          createFilterFor(query)) : $scope.dataAcquisitionProjects;
      return results;
    };

    //Update the state for the current project
    $scope.updateCurrentProject = function(project) {
      $scope.project = project;
      CurrentProjectService.setCurrentProject(project);
    };

    //Deletes the current project
    $scope.deleteProject = function() {
      $scope.showDeleteConfirm();
    };

    /* Function for opening a dialog for creating a new project */
    $scope.createProject = function() {
      $mdDialog.show({
        controller: 'CreateProjectDialogController',
        templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
          'views/create-project-dialog.html.tmpl',
        clickOutsideToClose: true
      })
      .then(function(project) {
        DataAcquisitionProjectResource.save({id: project.name},
          //Success
          function() {
            $scope.loadProjects();
            SimpleMessageToastService
              .openSimpleMessageToast('metadatamanagementApp.' +
              'dataAcquisitionProject.detail.logMessages.' +
              'dataAcquisitionProject.saved', project.name);
          },
          //Server Error
          function(errorMsg) {
            SimpleMessageToastService
              .openSimpleMessageToast('metadatamanagementApp.' +
              'dataAcquisitionProject.detail.logMessages.' +
              'dataAcquisitionProject.serverError' + errorMsg);
            $scope.loadProjects();
          }
        );
      });
    };

    //Conformation Dialog for the deletion of a project
    $scope.showDeleteConfirm = function() {
      var confirm = $mdDialog.confirm()
            .title($translate.instant('metadatamanagementApp.' +
              'dataAcquisitionProject.detail.logMessages.' +
              'dataAcquisitionProject.deleteTitle', {name: $scope.project.id}))
            .textContent($translate.instant('metadatamanagementApp.' +
              'dataAcquisitionProject.detail.logMessages.' +
              'dataAcquisitionProject.delete', {name: $scope.project.id}))
            .ariaLabel($translate.instant('metadatamanagementApp.' +
              'dataAcquisitionProject.detail.logMessages.' +
              'dataAcquisitionProject.delete', {name: $scope.project.id}))
            .ok($translate.instant('global.buttons.ok'))
            .cancel($translate.instant('global.buttons.cancel'));

      $mdDialog.show(confirm).then(function() {
        //User clicked okay -> Delete Project, hide dialog, show feedback
        $mdDialog.hide($scope.project.id);
        DataAcquisitionProjectResource.delete({id: $scope.project.id});
        CurrentProjectService.setCurrentProject(null);
        $scope.loadProjects();
        SimpleMessageToastService.openSimpleMessageToast(
            'metadatamanagementApp.' +
            'dataAcquisitionProject.detail.logMessages.' +
            'dataAcquisitionProject.deletedSuccessfullyProject',
            $scope.project.id);
      }, function() {
        //User clicked cancel
        $mdDialog.cancel();
      });
    };

    $scope.postValidateProject = function() {
      DataAcquisitionProjectPostValidationService
        .postValidate($scope.project.id);
    };

    //Functions for toggling buttons.
    $scope.toggleAccountMenu = function() {
      $scope.isAccountMenuOpen = !$scope.isAccountMenuOpen;
    };

    $scope.toggleAdminMenu = function() {
      $scope.isAdminMenuOpen = !$scope.isAdminMenuOpen;
    };

  });

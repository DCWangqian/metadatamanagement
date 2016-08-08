/* Author: Daniel Katzberg */
/* global _ */
'use strict';

/* The Controller for the search. It differs between tabs and a tab represent
a result of a type like variable or dataSet and so on. */
angular.module('metadatamanagementApp').controller('SearchController',
    function($scope, Principal, ElasticSearchProperties, $location,
        AlertService, SearchDao, $translate, VariableUploadService,
        AtomicQuestionUploadService, DataSetUploadService,
        SurveyUploadService, $mdDialog, CleanJSObjectService,
        CurrentProjectService) {

        //Check the login status
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
          });

        $scope.currentProject = CurrentProjectService.getCurrentProject();
        $scope.page = {
          currentPageNumber: $location.search().page || 1,
          totalHits: 0,
          size: 5
        };

        $scope.searchResult = {};
        $scope.isInitializing = true;

        //Need for interpretation of the query element in the url.
        $scope.query = $location.search().query;

        //Information for the different tabs
        $scope.tabs = [{
            title: 'search.tabs.all',
            inputLabel: 'search.input-label.all',
            elasticSearchType: '',
            count: null
          }, {
            title: 'search.tabs.variables',
            inputLabel: 'search.input-label.variables',
            icon: 'assets/images/icons/variable.svg',
            elasticSearchType: 'variables',
            count: null
          }, {
            title: 'search.tabs.questions',
            inputLabel: 'search.input-label.questions',
            icon: 'assets/images/icons/question.svg',
            elasticSearchType: 'atomic_questions',
            count: null
          }, {
            title: 'search.tabs.surveys',
            inputLabel: 'search.input-label.surveys',
            icon: 'assets/images/icons/survey.svg',
            elasticSearchType: 'surveys',
            count: null
          }, {
            title: 'search.tabs.data-sets',
            inputLabel: 'search.input-label.data-sets',
            icon: 'assets/images/icons/data-set.svg',
            elasticSearchType: 'data_sets',
            count: null
          }];

        //The current index of the active tab
        $scope.selectedTabIndex = 0;
        for (var i = 0; i < $scope.tabs.length; i++) {
          if ($scope.tabs[i].elasticSearchType === $location.search().type) {
            $scope.selectedTabIndex = i;
          }
        }

        //Search function
        $scope.search = function() {
            $scope.isSearching = true;
            var selectedTab = $scope.tabs[$scope.selectedTabIndex];

            $scope.tabs.forEach(function(tab) {
                tab.count = null;
              });

            //Search with different types, binded on every tab
            if ($scope.currentProject) {
              $location.search('rdc-project', $scope.currentProject.id);
            } else {
              $location.search('rdc-project', '');
            }
            $location.search('query', $scope.query);
            $location.search('page', $scope.page.currentPageNumber);
            $location.search('type', selectedTab.elasticSearchType);

            SearchDao.search($scope.query, $scope.page.currentPageNumber,
              $scope.currentProject,
              selectedTab.elasticSearchType, $scope.page.size)
                .then(function(data) {
                    $scope.searchResult = data.hits.hits;
                    $scope.page.totalHits = data.hits.total;
                    selectedTab.count = data.hits.total;

                    //Count information by aggregations
                    if (selectedTab.elasticSearchType === '') {
                      $scope.tabs.forEach(function(tab) {
                          if (tab.elasticSearchType !== '') {
                            tab.count = 0;
                          }
                          data.aggregations.countByType.buckets.forEach(
                              function(bucket) {
                                  if (bucket.key === tab.elasticSearchType) {
                                    // jscs:disable
                                      tab.count = bucket.doc_count;
                                      // jscs:enable
                                  }
                                });
                        });
                    }
                    $scope.isSearching = false;
                    //If something going wrong: send an alert
                  }, function(error) {
                    AlertService.error(error.message);
                    console.trace(error);
                    $scope.isSearching = false;
                  });
          };

        $scope.onTabSelected = function() {
            //this funtion is called even when the dialog is initialized
            if (!$scope.isInitializing) {
              $scope.page.currentPageNumber = 1;
            }
            $scope.isInitializing = false;
            $scope.search();
          };

        $scope.onQueryChanged = function() {
          $scope.page.currentPageNumber = 1;
          $scope.throttledSearch();
        };

        $scope.$on('current-project-changed', function(event, currentProject) {
          $scope.currentProject = currentProject;
          $scope.page.currentPageNumber = 1;
          $scope.search();
        });

        $scope.throttledSearch = _.throttle($scope.search, 500);

        $scope.uploadVariables = function(file) {
            if (!file) {
              return;
            }
            var dataAcquisitionProject = $scope.currentProject;
            if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProject)) {
              var confirm = $mdDialog.confirm()
                  .title($translate.instant(
                      'search.deleteMessages.deleteVariablesTitle'))
                  .textContent($translate.instant(
                      'search.deleteMessages.deleteVariables', {
                          id: dataAcquisitionProject.id
                        }))
                  .ariaLabel($translate.instant(
                      'search.deleteMessages.deleteVariables', {
                          id: dataAcquisitionProject.id
                        }))
                  .ok($translate.instant('global.buttons.ok'))
                  .cancel($translate.instant('global.buttons.cancel'));
              $mdDialog.show(confirm).then(function() {
                  //start upload and open log toast
                  VariableUploadService
                      .uploadVariables(file, dataAcquisitionProject.id);

                  //Cancel. Nothing happens
                }, function() {});
            }
          };

        $scope.uploadQuestions = function(file) {
            if (!file) {
              return;
            }
            var dataAcquisitionProject = $scope.currentProject;
            if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProject)) {
              var confirm = $mdDialog.confirm()
                  .title($translate.instant(
                      'search.deleteMessages.deleteQuestionsTitle'))
                  .textContent($translate.instant(
                      'search.deleteMessages.deleteQuestions', {
                          id: dataAcquisitionProject.id
                        }))
                  .ariaLabel($translate.instant(
                      'search.deleteMessages.deleteQuestions', {
                          id: dataAcquisitionProject.id
                        }))
                  .ok($translate.instant('global.buttons.ok'))
                  .cancel($translate.instant('global.buttons.cancel'));
              $mdDialog.show(confirm).then(function() {
                  //start upload
                  AtomicQuestionUploadService
                      .uploadAtomicQuestions(file, dataAcquisitionProject.id);
                  //Cancel. Nothing happens
                }, function() {});
            }
          };

        $scope.uploadSurveys = function(file) {
            if (!file) {
              return;
            }
            var dataAcquisitionProject = $scope.currentProject;
            if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProject)) {
              var confirm = $mdDialog.confirm()
                  .title($translate.instant(
                      'search.deleteMessages.deleteSurveysTitle'))
                  .textContent($translate.instant(
                      'search.deleteMessages.deleteSurveys', {
                          id: dataAcquisitionProject.id
                        }))
                  .ariaLabel($translate.instant(
                      'search.deleteMessages.deleteSurveys', {
                          id: dataAcquisitionProject.id
                        }))
                  .ok($translate.instant('global.buttons.ok'))
                  .cancel($translate.instant('global.buttons.cancel'));
              $mdDialog.show(confirm).then(function() {
                  //start upload
                  SurveyUploadService
                      .uploadSurveys(file, dataAcquisitionProject.id);
                  //Cancel. Nothing happens
                }, function() {});
            }
          };

        $scope.uploadDataSets = function(file) {
            if (!file) {
              return;
            }
            var dataAcquisitionProject = $scope.currentProject;
            if (!CleanJSObjectService.isNullOrEmpty(dataAcquisitionProject)) {
              var confirm = $mdDialog.confirm()
                  .title($translate.instant(
                      'search.deleteMessages.deleteDataSetsTitle'))
                  .textContent($translate.instant(
                      'search.deleteMessages.deleteDataSets', {
                          id: dataAcquisitionProject.id
                        }))
                  .ariaLabel($translate.instant(
                      'search.deleteMessages.deleteDataSets', {
                          id: dataAcquisitionProject.id
                        }))
                  .ok($translate.instant('global.buttons.ok'))
                  .cancel($translate.instant('global.buttons.cancel'));
              $mdDialog.show(confirm).then(function() {
                  //start upload
                  DataSetUploadService
                      .uploadDataSets(file, dataAcquisitionProject.id);
                  //Cancel. Nothing happens
                }, function() {});
            }
          };

        //Refresh function for the refresh button
        $scope.refresh = function() {
            $scope.search();
          };
      });

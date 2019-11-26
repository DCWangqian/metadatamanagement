/* global _ */
'use strict';

angular.module('metadatamanagementApp')
  .controller('InstrumentDetailController',
    function(entity, InstrumentAttachmentResource, MessageBus,
             PageTitleService, LanguageService, $state, CleanJSObjectService,
             ToolbarHeaderService, Principal, SimpleMessageToastService,
             SearchResultNavigatorService,
             DataAcquisitionProjectResource, ProjectUpdateAccessService,
             InstrumentSearchService, OutdatedVersionNotifier, $stateParams,
             blockUI) {
      blockUI.start();

      SearchResultNavigatorService
        .setSearchIndex($stateParams['search-result-index']);

      SearchResultNavigatorService.registerCurrentSearchResult();
      var activeProject;
      //Controller Init
      var ctrl = this;
      ctrl.isAuthenticated = Principal.isAuthenticated;
      ctrl.hasAuthority = Principal.hasAuthority;
      ctrl.searchResultIndex = SearchResultNavigatorService.getSearchIndex();
      ctrl.counts = {};
      ctrl.survey = null;
      ctrl.attachments = null;
      ctrl.study = null;
      // ctrl.questionCount = null;
      ctrl.projectIsCurrentlyReleased = true;
      ctrl.enableJsonView = Principal
        .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_ADMIN']);

      ctrl.jsonExcludes = [
        'nestedStudy',
        'nestedSurveys',
        'nestedQuestions',
        'nestedVariables',
        'nestedDataSets',
        'nestedRelatedPublications',
        'nestedConcepts'
      ];

      //Wait for instrument Promise
      entity.promise.then(function(result) {
        var fetchFn = InstrumentSearchService.findShadowByIdAndVersion
          .bind(null, result.masterId);
        OutdatedVersionNotifier.checkVersionAndNotify(result, fetchFn);

        if (Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          DataAcquisitionProjectResource.get({
            id: result.dataAcquisitionProjectId
          }).$promise.then(function(project) {
            ctrl.projectIsCurrentlyReleased = (project.release != null);
            ctrl.assigneeGroup = project.assigneeGroup;
            activeProject = project;
          });
        }
        ToolbarHeaderService.updateToolbarHeader({
          'stateName': $state.current.name,
          'id': result.id,
          'number': result.number,
          'instrumentIsPresent': true,
          'surveys': result.surveys,
          'studyId': result.studyId,
          'studyIsPresent': CleanJSObjectService.isNullOrEmpty(result.study) ?
            false : true,
          'projectId': result.dataAcquisitionProjectId,
          'version': result.shadow ? _.get(result, 'release.version') : null
        });
        var currenLanguage = LanguageService.getCurrentInstantly();
        var secondLanguage = currenLanguage === 'de' ? 'en' : 'de';
        if (!Principal.isAuthenticated()) {
          MessageBus.set('onDataPackageChange',
            {
              masterId: result.study.masterId,
              version: result.release.version
            });
        }
        PageTitleService.setPageTitle('instrument-management.' +
          'detail.page-title', {
          description: result.description[currenLanguage] ?
            result.description[currenLanguage] :
            result.description[secondLanguage],
          instrumentId: result.id
        });
        if (result.dataSets) {
          ctrl.accessWays = [];
          result.dataSets.forEach(function(dataSet) {
            ctrl.accessWays = _.union(dataSet.accessWays, ctrl.accessWays);
          });
        }
        if (result.release || Principal
          .hasAnyAuthority(['ROLE_PUBLISHER', 'ROLE_DATA_PROVIDER'])) {
          ctrl.instrument = result;
          //load all related objects in parallel
          InstrumentAttachmentResource.findByInstrumentId({
            instrumentId: ctrl.instrument.id
          }).$promise.then(
            function(attachments) {
              if (attachments.length > 0) {
                ctrl.attachments = attachments;
              }
            });
          ctrl.counts.surveysCount = result.surveys.length;
          if (ctrl.counts.surveysCount === 1) {
            ctrl.survey = result.surveys[0];
          }

          ctrl.study = result.study;

          ctrl.counts.questionsCount = result.questions.length;
          if (ctrl.counts.questionsCount === 1) {
            ctrl.question = result.questions[0];
          }

          ctrl.counts.publicationsCount = result.relatedPublications.length;
          if (ctrl.counts.publicationsCount === 1) {
            ctrl.relatedPublication = result.relatedPublications[0];
          }

          ctrl.counts.conceptsCount = result.concepts.length;
          if (ctrl.counts.conceptsCount === 1) {
            ctrl.concept = result.concepts[0];
          }
        } else {
          SimpleMessageToastService.openAlertMessageToast(
            'instrument-management.detail.not-released-toast', {id: result.id}
          );
        }
      }).finally(blockUI.stop);

      ctrl.instrumentEdit = function() {
        if (ProjectUpdateAccessService
          .isUpdateAllowed(activeProject, 'instruments', true)) {
          $state.go('instrumentEdit', {id: ctrl.instrument.id});
        }
      };
    });

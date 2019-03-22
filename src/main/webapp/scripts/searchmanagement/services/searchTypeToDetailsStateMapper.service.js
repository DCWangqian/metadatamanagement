'use strict';

angular.module('metadatamanagementApp').factory(
  'SearchTypeToDetailsStateMapper',
  function() {
    function getDetailStateUrl(type) {
      switch (type) {
        case 'studies': return 'studyDetail';
        case 'surveys': return 'surveyDetail';
        case 'instruments': return 'instrumentDetail';
        case 'questions': return 'questionDetail';
        case 'data_sets': return 'dataSetDetail';
        case 'variables': return 'variableDetail';
        case 'related_publications': return 'relatedPublicationDetail';
      }
    }

    return {
      getDetailStateUrl: getDetailStateUrl
    };
  }
);

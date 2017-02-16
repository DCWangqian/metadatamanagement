'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'data-acquisition-project-management': {
        'name': 'Name of the Data Acquisition Projects',
        'home': {
          'title': 'Data Acquisition Projects',
          'createLabel': 'Create a new Data Acquisition Project'
        },
        'delete': {
          'question': 'Are you sure you want to delete Data Acquisition Project "{{ name }}"?'
        },
        'log-messages': {
          'data-acquisition-project': {
            'saved': 'Successfully saved the Data Acquisition Project "{{ id }}"!',
            'server-error': 'An error on the server has occurred: ',
            'delete-title': 'Delete Project "{{ id }}"?',
            'delete': 'Do you really want to delete the Project "{{ id }}"? This cannot be undone.',
            'deleted-successfully-project': 'Successfully deleted Data Acquisition Project "{{ id }}"!',
            'deleted-not-successfully-project': 'Could not delete Data Acquisition Project "{{ id }}"!'
          }
        },
        'error': {
          'data-acquisition-project': {
            'id': {
              'not-empty': 'The RDC-ID of Data Acquisition Project must not be empty!',
              'pattern': 'The RDC-ID must contain only alphanumeric signs, german umlauts and ß.',
              'size': 'The max length of the RDC-ID is 32 signs.'
            }
          },
          'post-validation': {
            'project-has-no-study': 'The Project with the RDC-ID {{ id }} has no study.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });

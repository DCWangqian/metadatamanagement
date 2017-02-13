'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'survey-management': {
        'log-messages': {
          'survey': {
            'saved': 'Survey with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Survey with RDC-ID {{ id }} has not been saved!',
            'missing-number': 'Survey {{ index }} does not contain a Number and has not been saved!',
            'unable-to-upload-image-file': 'Image file "{{ file }}" could not be uploaded!',
            'unable-to-read-image-file': 'Image file "{{ file }}" could not be read!',
            'upload-terminated': 'Finished upload of {{ totalSurveys }} Surveys and {{ totalImages }} Images with {{ totalErrors }} errors.',
            'unable-to-delete': 'The Surveys could not be deleted!',
            'duplicate-survey-number': 'The number ({{ number }}) of Survey {{ index }} has already been used.',
            'cancelled': 'Surveys upload cancelled!'
          }
        },
        'detail': {
          'title': '{{ title }} ({{ surveyId }})',
          'survey': 'Survey',
          'surveys': 'Surveys',
          'surveys-same-study': 'All Surveys of the same Study',
          'survey-informations': 'Survey Informations',
          'related-information': 'Related Information',
          'related-objects': 'Related Objects',
          'field-period': 'Field Period',
          'population': 'Population',
          'survey-method': 'Survey Method',
          'sample': 'Sample',
          'not-found': 'The {{id}} references to an unknown Survey.',
          'not-found-references': 'The id {{id}} has no References to Surveys.',
          'no-related-surveys': 'No related Surveys.',
          'related-surveys': 'Related Surveys',
          'grossSampleSize': 'Gross Sample Size',
          'sampleSize': 'Net Sample Size',
          'responseRate': 'Response Rate',
          'response-rate-informations': 'Further information about the Response Rate'
        },
        'error': {
          'survey': {
            'id': {
              'valid-survey-id-name': 'The RDC-ID of the Survey is not valid for the Pattern: sur-RDCID-sy{Number}! .',
              'not-empty': 'The RDC-ID of the Survey must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, excemation sign and minus for the RDC-ID.'
            },
            'title': {
              'i18n-string-size': 'The max length of the title is 128 signs.'
            },
            'field-period': {
              'not-null': 'The Field Period of a Survey must not be empty!'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Survey must not be empty!'
              }
            },
            'population': {
              'not-null': 'The Population of a Survey must not be empty!',
              'i18n-string-not-empty': 'The Population of the Survey has to be given for one language.',
              'i18n-string-size': 'The max length of the population of the survey is 2048 signs.'
            },
            'sample': {
              'not-null': 'The Sample of a Survey must not be empty!',
              'i18n-string-not-empty': 'The Sample of the Survey has to be given for one language.',
              'i18n-string-size': 'The max length of the sample of the survey is 2048 signs.'
            },
            'survey-method': {
              'not-null': 'The Survey-Method must not be empty!',
              'i18n-string-not-empty': 'The Survey-Method has to be given for one language.',
              'i18n-string-size': 'The max length of the Survey-Method is 128 signs.'
            },
            'gross-sample-size': {
              'not-null': 'The Gross Sample Size of a Survey must not be empty!'
            },
            'sample-size': {
              'not-null': 'The Sample Size of a Survey must not be empty!'
            },
            'unique-survey-number': 'The Number of a Survey has to be unique within a Data Acquisition Project!',
            'number': {
              'not-null': 'The Number of the Survey must not be empty!'
            },
            'response-rate': {
              'not-null': 'The Response Rate of a Survey must not be empty!'
            }
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });

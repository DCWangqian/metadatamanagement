'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'study-management': {
        'detail': {
          'title': '{{ title }} ({{ studyId }})',
          'study': 'Study',
          'studies': 'Studies',
          'study-informations': 'Study Detail',
          'related-information': 'Related Information',
          'not-found': 'The {{id}} references to an unknown Study.',
          'not-found-references': 'The id {{id}} has no References to Studies.',
          'description': 'Study Description',
          'survey-details': 'Survey Details',
          'data-set-details': 'Data Set Informationen',
          'instrument-information': 'Instrument Informations',
          'surveySeries': 'Survey Series',
          'institution': 'Institution',
          'authors': 'Authors',
          'sponsors': 'Sponsored by',
          'accessWays': 'Access Ways',
          'releases': 'Releases',
          'version': 'Version',
          'notes': 'Botes',
          'citationHint': 'Citation Hint',
          'instruments': 'Instruments',
          'no-related-studies': 'No related Studies.',
          'related-studies': 'Related Studies',
          'available-data-products': 'Available Data Products',
          'basic-data-of-surveys': 'Basic Data of Surveys',
          'downloads': 'Downloads',
          'not-released-toast': 'Study "{{ id }}" has not yet been released to all users!',
          'tooltips': {
            'surveys': {
              'one': 'Click to show the survey of this study',
              'many': 'Click to show all surveys of this study'
            },
            'data-sets': {
              'one': 'Click to show the data set of this study',
              'many': 'Click to show all data sets of this study',
            },
            'publications': {
              'one': 'Click to show the publication related to this study',
              'many': 'Click to show all publications related to this study'
            }
          },
          'data-set': {
            'card-title': 'Available Data Sets',
            'accessWays': 'Access Ways',
            'description': 'Description',
            'description-tooltip': 'Click to show data set "{{id}}"',
            'maxNumberOfObservations': 'Observations'
          }
        },
        'log-messages': {
          'study': {
            'saved': 'Study with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Study with RDC-ID {{ id }} has not been saved!',
            'study-file-not-found': 'The selected directory does not contain the following file: study.xlsx!',
            'releases-file-not-found': 'The selected directory does not contain the following file: releases.xlsx!',
            'unable-to-delete': 'The study could not be deleted!',
            'missing-id': 'Study {{ index }} does not contain a RDC-ID and has not been saved:',
            'upload-terminated': 'Finished upload of {{ total }} Study with {warnings }} warnings and {{ errors }} errors.',
            'cancelled': 'Study upload cancelled!'
          }
        },
        'error': {
          'study': {
            'id': {
              'not-empty': 'The RDC-ID of the Study must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, exclamation mark and minus for the RDC-ID.',
              'not-valid-id': 'The study id must be equal to the id scheme "stu-" + {ProjectId} + "!" .'
            },
            'title': {
              'not-null': 'The title of the study must not be empty!',
              'i18n-string-size': 'The max length of the study title is 2048.',
              'i18n-string-not-empty': 'The title of the study must not be empty at least for one language.'
            },
            'description': {
              'not-null': 'The description of the study must not be empty!',
              'i18n-string-size': 'The max length of the study description is 2048.',
              'i18n-string-not-empty': 'The description of the study must not be empty at least for one language.'
            },
            'institution': {
              'not-null': 'The institution of the study must not be empty!',
              'i18n-string-size': 'The max length of the institution is 128.',
              'i18n-string-not-empty': 'The institution of the study must not be empty at least for one language.'
            },
            'sponsor': {
              'i18n-string-size': 'The max length of the sponsor of the study is 128.'
            },
            'citation-hint': {
              'i18n-string-size': 'The max length of the citation hint of the study is 2048.'
            },
            'survey-series': {
              'i18n-string-size': 'The max length of the survey series is 128 signs.'
            },
            'data-availability': {
              'not-null': 'The data avaibility of the study must not be empty!',
              'valid-data-availability': 'The allowed values for data availability of the study are: Available, In preparation, Not available.'
            },
            'survey-design': {
              'not-null': 'The survey design of the study must not be empty!',
              'valid-survey-design': 'The allowed values for the survey design of the study are: Cross-section, Panel.'
            },
            'access-ways': {
              'not-empty': 'The list of access ways of a variable needs min. one element and must not be empty!',
              'valid-access-ways': 'The values of access ways are invalid. Valid values are: download-cuf, download-suf, remote-desktop-suf, onsite-suf, not-accessible.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Study must not be empty!'
              }
            }
          },
          'release': {
            'version': {
              'not-empty': 'The version of a release must not be empty!',
              'size': 'The max length of the version is 32 signs.',
              'pattern': 'Use only numbers and the dot for the version.'
            },
            'doi': {
              'size': 'The max length of the doi is 128 signs.'
            },
            'date': {
              'not-null': 'The date of a Release must not be empty!'
            },
            'notes': {
              'size': 'The max length of the version is 2048 signs.'
            }
          },
          'post-validation': {
            'study-has-an-accessway-which-was-not-found-in-sub-data-sets': 'The study {{id}} has an accessway ({{toBereferenzedId}}) which was not found in Sub-Datasets.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });

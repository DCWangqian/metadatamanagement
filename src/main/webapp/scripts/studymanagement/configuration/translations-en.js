'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'study-management': {
        'detail': {
          'label': {
            'study': 'Study',
            'studies': 'Studies',
            'surveySeries': 'Survey Series',
            'institution': 'Institution',
            'authors': 'Authors',
            'sponsors': 'Sponsored by',
            'surveyDesign': 'Survey Design',
            'annotations': 'Annotations',
            'wave': 'Available Waves',
            'survey-data-type': 'Survey Data Type',
            'version': 'Version of Data Sets',
            'title': 'Title',
            'dataAvailability': 'Data Availability',
            'attachments': {
              'type': 'Type',
              'title': 'Title',
              'description': 'Description',
              'language': 'Document Language',
              'file': 'File'
            },
            'data-set': {
              'accessWays': 'Access Ways',
              'description': 'Description',
              'description-tooltip': 'Click to show data set "{{id}}"',
              'maxNumberOfObservations': 'Observations'
            },
            'doi': 'DOI'
          },
          'attachments': {
            'table-title': 'Documents related to the Study'
          },
          'data-set': {
            'card-title': 'Available Data Sets'
          },
          'title': '{{ title }} ({{ studyId }})',
          'description': 'Study Description',
          'basic-data-of-surveys': 'Basic Data of Surveys',
          'not-found': 'The {{id}} references to an unknown Study.',
          'not-found-references': 'The id {{id}} has no References to Studies.',
          'not-yet-released': 'Not yet released',
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
            },
            'variables': {
              'one': 'Click to show the variable of this study',
              'many': 'Click to show all variables of this study'
            },
            'questions': {
              'one': 'Click to show the question of this study',
              'many': 'Click to show all questions of this study'
            },
            'instruments': {
              'one': 'Click to show the instruments of this study',
              'many': 'Click to show all instruments of this study'
            },
            'studies': {
              'survey-series': 'Click to show all studies of the survey series.'
            }
          },
          'doi-tooltip': 'Click to open the DOI in a new Tab'
        },
        'log-messages': {
          'study': {
            'saved': 'Study with RDC-ID {{ id }} was saved successfully!',
            'not-saved': 'Study with RDC-ID {{ id }} has not been saved!',
            'study-file-not-found': 'The selected directory does not contain the following file: study.xlsx!',
            'releases-file-not-found': 'The selected directory does not contain the following file: releases.xlsx!',
            'unable-to-delete': 'The study could not be deleted!',
            'upload-terminated': 'Finished upload of {{ total }} Study and {{ attachments }} Attachments with {{ warnings }} warnings and {{ errors }} errors.',
            'cancelled': 'Study upload cancelled!'
          },
          'study-attachment': {
            'not-saved': 'Attachment "{{ id }}" has not been saved:',
            'file-not-found': 'The File {{ filename }} was not found and has not been saved.'
          }
        },
        'error': {
          'study': {
            'id': {
              'not-empty': 'The RDC-ID of the Study must not be empty!',
              'size': 'The max length of the RDC-ID is 128 signs.',
              'pattern': 'Use only alphanumeric signs, german umlauts, ß and space, underscore, exclamation mark and minus for the RDC-ID.',
              'not-valid-id': 'The study id must be equal to the id scheme "stu-" + {ProjectId} + "$" .'
            },
            'title': {
              'not-null': 'The title of the study must not be empty!',
              'i18n-string-size': 'The max length of the study title is 2048.',
              'i18n-string-entire-not-empty': 'The title of the study must not be empty for all languages.'
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
            'survey-series': {
              'i18n-string-size': 'The max length of the survey series is 128 signs.'
            },
            'data-availability': {
              'not-null': 'The data availability of the study must not be empty!',
              'valid-data-availability': 'The allowed values for data availability of the study are: Available, In preparation, Not available.'
            },
            'survey-design': {
              'not-null': 'The survey design of the study must not be empty!',
              'valid-survey-design': 'The allowed values for the survey design of the study are: Cross-Section, Panel.'
            },
            'authors': {
              'not-empty': 'The list of authors of a study needs min. one element and must not be empty!',
            },
            'annotations': {
              'i18n-string-size': 'The max length of the annotations is 2048 signs.'
            },
            'data-acquisition-project': {
              'id': {
                'not-empty': 'The RDC-ID of the Data Acquisition Project for the Study must not be empty!'
              }
            }
          },
          'study-attachment-metadata': {
            'study-id': {
              'not-empty': 'The ID of the corresponding Study must not be empty.'
            },
            'project-id': {
              'not-empty': 'The ID of the Data Acquisition Project must not be empty!'
            },
            'type': {
              'not-null': 'The type of the attachment must not be empty!',
              'i18n-string-size': 'The type is mandatory in both languages and must not contain more than 32 characters.',
              'valid-type': 'The type must be one of the following: Method Report, Other.'
            },
            'description': {
              'not-null': 'The description of the attachment must not be empty!',
              'i18n-string-size': 'The description is mandatory and must in at least one language and must not contain more than 128 characters.',
              'i18n-string-not-empty': 'The description must not be empty!'
            },
            'title': {
              'string-size': 'The title of the attachment must not contain more than 2048 characters.'
            },
            'language': {
              'not-null': 'The language of the attachment must not be empty!',
              'not-supported': 'The language of the attachment must be a two-letter abbreviation according to ISO 639-1!'
            },
            'filename': {
              'not-empty': 'The filename of the attachment must not be empty!'
            }
          }
        },
        'edit': {
          'edit-page-title': 'Edit Study {{studyId}}',
          'create-page-title': 'Create Study {{studyId}}',
          'success-on-save-toast': 'Study {{studyId}} has been saved successfully.',
          'error-on-save-toast': 'An error occurred during saving of Study {{studyId}}!',
          'previous-version-restored-toast': 'Previous version of Studie {{ studyId }} can be saved now.',
          'label': {
            'edit-study': 'Edit Study:',
            'create-study': 'Create Study:',
            'first-name': 'First Name',
            'middle-name': 'Middle Name',
            'last-name': 'Last Name'
          },
          'open-choose-previous-version-tooltip': 'Click for restoring a previous version of this study.',
          'save-tooltip': 'Click to save this study.',
          'choose-previous-version': {
            'title': 'Previous Versions of Study {{ studyId }}',
            'cancel-tooltip': 'Click to return without choosing a study.',
            'no-versions-found': 'There are no previous versions of study {{ studyId }}.',
            'study-title': 'Title',
            'lastModifiedDate': 'Saved at',
            'lastModifiedBy': 'Saved by'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('en', translations);
  });

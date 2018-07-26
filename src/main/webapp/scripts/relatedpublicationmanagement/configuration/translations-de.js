'use strict';

angular.module('metadatamanagementApp').config(
  function($translateProvider) {
    var translations = {
      //jscs:disable
      'related-publication-management': {
        'home': {
          'title': 'Publikation'
        },
        'log-messages': {
          'related-publication': {
            'saved': 'Die Publikation mit FDZ-ID {{ id }} erfolgreich gespeichert!',
            'not-saved': 'Die Publikation mit FDZ-ID {{ id }} wurde nicht gespeichert!',
            'missing-id': 'Der {{ index }}. Die Publikation enthält keine FDZ-ID und wurde nicht gespeichert!',
            'duplicate-id': 'Die FDZ-ID ({{ id }}) der {{ index }}. Publikation wurde bereits verwendet.',
            'upload-terminated': 'Upload von {{ total }} Publikationen mit {{warnings}} Warnungen und {{ errors }} Fehlern beendet!',
            'unable-to-delete': 'Die Publikationen konnten nicht gelöscht werden!',
            'cancelled': 'Upload von Publikationen abgebrochen'
          }
        },
        'detail': {
          'label': {
            'publication': 'Publikation',
            'publications': 'Publikationen',
            'doi': 'DOI',
            'sourceReference': 'Quellenangabe',
            'sourceLink': 'URL',
            'authors': 'Autor(inn)en',
            'year': 'Erscheinungsjahr',
            'source-reference': 'Referenz',
            'abstract-source': 'Quelle'
          },
          'abstract': 'Abstract',
          'title': '{{ title }} ({{publicationId}})',
          'doi-tooltip': 'Klicken, um die DOI in einem neuen Tab zu öffnen',
          'sourceLink-tooltip': 'Klicken, um die Quelle dieser Publikation in einem neuen Tab zu öffnen ',
          'tooltips': {
            'surveys': {
              'one': 'Klicken, um die Erhebung anzuzeigen, zu der diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Erhebungen anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'data-sets': {
              'one': 'Klicken, um den Datensatz anzuzeigen, zu dem diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Datensätze anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'questions': {
              'one': 'Klicken, um die Frage anzuzeigen, zu der diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Fragen anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'instruments': {
              'one': 'Klicken, um das Instrument anzuzeigen, zu dem diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Instrumente anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'variables': {
              'one': 'Klicken, um die Variable anzuzeigen, zu der diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Variablen anzuzeigen, zu denen diese Publikation verfasst wurde'
            },
            'studies':{
              'one': 'Klicken, um die Studie anzuzeigen, zu der diese Publikation verfasst wurde',
              'many': 'Klicken, um alle Studien anzuzeigen, zu denen diese Publikation verfasst wurde'
            }
          }
        },
        'error': {
          'related-publication': {
            'one-foreign-key-is-used': 'Die Publikation hat keine Verknüpfung zu einem anderen Objekt.',
            'one-study-or-study-series-is-used': 'Die Publikation hat keine Verknüpfung zu einer Studie oder einer Studienreihe',
            'valid-related-publication-id': 'Die Id einer Publikation muss dem Muster "pub-" + {IdAusCitavi} + "$".',
            'study-exists': 'Es gibt keine Studie mit der FDZ-ID "{{invalidValue}}"!',
            'survey-exists': 'Es gibt keine Erhebung mit der FDZ-ID "{{invalidValue}}"!',
            'dataset-exists': 'Es gibt keinen Datensatz mit der FDZ-ID "{{invalidValue}}"!',
            'variable-exists': 'Es gibt keine Variable mit der FDZ-ID "{{invalidValue}}"!',
            'instrument-exists': 'Es gibt kein Instrument mit der FDZ-ID "{{invalidValue}}"!',
            'question-exists': 'Es gibt keine Frage mit der FDZ-ID "{{invalidValue}}"!',
            'study-series-exists': 'Es gibt keine Studie mit der Studienreihe "{{invalidValue.de}}"!',
            'id': {
              'not-empty': 'Die FDZ-ID der Publikation darf nicht leer sein!',
              'size': 'Die Maximallänge der FDZ-ID ist 512 Zeichen.',
              'pattern': 'Die FDZ-ID darf keine Leerzeichen enthalten.'
            },
            'source-reference': {
              'not-empty': 'Die Source-Referenz der Publikation darf nicht leer sein!',
              'size': 'Die Maximallänge der Source-Referenz der Publikation ist 2048 Zeichen.'
            },
            'publication-abstract': {
              'size': 'Die Maximallänge des Abstrakts der Publikation ist 1048576 Zeichen.'
            },
            'doi': {
              'size': 'Die Maximallänge der DOI der Publikation ist 512 Zeichen.'
            },
            'source-link': {
              'pattern': 'Der Link der Quelle der Publikation ist keine gültige URL'
            },
            'title': {
              'not-empty': 'Der Titel der Publikation darf nicht leer sein!',
              'size': 'Die Maximallänge des Titels der Publikation ist 2048 Zeichen.'
            },
            'authors': {
              'size': 'Die Maximallänge der Autor(inn)en der Publication ist 2048 Zeichen.',
              'not-empty': 'Die Autoren der Publikation darf nicht leer sein!'
            },
            'year': {
              'not-null': 'Erscheinungsjahr darf nicht leer sein!',
              'valid': 'Erscheinungsjahr muss zwischen 1960 und {{currentDate | date :"yyyy"}} sein!.'
            },
            'abstract-source': {
              'i18n-string-size': 'Die Maximallänge der Quelle der Publication ist 2048 Zeichen.'
            },
            'language': {
              'not-null': 'Die Sprache der Publikation darf nicht leer sein!',
              'not-supported': 'Die Sprache muss eine gültige zweibuchstabige Abkürzung gemäß ISO 639-1 sein.'
            }
          },
          'post-validation': {
            'study-unknown': 'Die Studie {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'variable-unknown': 'Die Variable {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'variable-has-not-a-referenced-study': 'Die Variable {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.',
            'survey-unknown': 'Die Erhebung {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'survey-has-not-a-referenced-study': 'Die Erhebung {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.',
            'data-set-unknown': 'Der Datensatz {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'data-set-has-not-a-referenced-study': 'Der Datensatz {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.',
            'instrument-unknown': 'Das Instrument {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'instrument-has-not-a-referenced-study': 'Das Instrument {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.',
            'question-unknown': 'Die Frage {{id}}, die bei der Publikation ({{toBereferenzedId}}) verlinkt ist, konnte nicht gefunden werden.',
            'question-has-not-a-referenced-study': 'Die Frage {{id}} referenziert auf eine Studie ({{additionalId}}), die nicht mit der Publikation ({{toBereferenzedId}}) verknüpft ist.'
          }
        }
      }
      //jscs:enable
    };
    $translateProvider.translations('de', translations);
  });

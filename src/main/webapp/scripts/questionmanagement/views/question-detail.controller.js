
'use strict';

angular.module('metadatamanagementApp')
  .controller('QuestionDetailController',

    function($scope, $rootScope, localStorageService,
      ShoppingCartService, $stateParams, DialogService,
      QuestionReferencedResource, blockUI, entity, $q) {
      $scope.question = entity;
      $scope.predecessors = [];
      $scope.successors = [];

      /* function to load only texts and ids for successors and predecessors */
      var loadSuccessorsQuestionTextOnly = function(items) {
        var deferred = $q.defer();
        var itemsAsString = '"' + items + '"';
        itemsAsString = itemsAsString.replace(/[\[\]'"]/g, '');
        QuestionReferencedResource.findByIdIn({ids: itemsAsString})
        .$promise.then(function(successors) {
          deferred.resolve(successors);
        });
        return deferred.promise;
      };
      /* function to load only texts and ids for successors and predecessors */
      var loadPredecessorsQuestionTextOnly = function(id) {
        var deferred = $q.defer();
        QuestionReferencedResource.findBySuccessorsContaining({id: id})
        .$promise.then(function(predecessors) {
          deferred.resolve(predecessors);
        });
        return deferred.promise;
      };

      var checkInvalidQuestionIds = function(allIds, customItems) {
        var tempQuestions;
        try {
          tempQuestions = [];
          allIds.forEach(function(id) {
            for (var i = 0; i < customItems.length; i++) {
              if (customItems[i].id === id) {
                tempQuestions.push(customItems[i]);
                break;
              }else {
                if (i === (customItems.length - 1)) {
                  var notFoundQuestion = {
                    id: id,
                    name: 'notFoundQuestion',
                    number: '-',
                    questionText: 'not-found'
                  };
                  tempQuestions.push(notFoundQuestion);
                }
              }
            }
          });
        } catch (e) {
          tempQuestions = [];
        }
        return tempQuestions;
      };
      $scope.$watch('question', function() {
        if ($scope.question.$resolved) {
          console.log($scope.question);
          loadSuccessorsQuestionTextOnly($scope.question.successors)
          .then(function(customSuccesors) {
            $scope.successors = checkInvalidQuestionIds(
              $scope.question.successors, customSuccesors._embedded.questions);
          });
          loadPredecessorsQuestionTextOnly($scope.question.id)
          .then(function(predecessors) {
            $scope.predecessors = predecessors._embedded.questions;
          });
        }
      }, true);

      /* function to open dialog for variables */
      $scope.showVariables = function() {
        blockUI.start();
        DialogService.showDialog($scope.question.id, 'variable');
      };

      /* get all items from localStorage */
      $scope.markedItems = ShoppingCartService.getShoppingCart();

      /* add new  item to localStorage */
      $scope.addToNotepad = function(id) {
        var lookup = {};
        for (var i = 0, len = $scope.markedItems.length; i < len; i++) {
          lookup[$scope.markedItems[i].id] = $scope.markedItems[i];
        }
        if (ShoppingCartService.searchInShoppingCart(id) === 'notFound') {
          $scope.markedItems.push({
            id: id,
            text: 'title',
            date:  new Date()
          });
          ShoppingCartService.addToShoppingCart($scope.markedItems);
        }
      };
    });

/* globals _ */
'use strict';

angular.module('metadatamanagementApp').config(function($stateProvider) {
  var commonStateConfig = {
    parent: 'site',
    reloadOnSearch: false,
    data: {
      authorities: []
    },
    views: {
      'content@': {
        templateUrl: 'scripts/ordermanagement/views/' +
          'shopping-cart.html.tmpl',
        controller: 'ShoppingCartController',
        controllerAs: 'ctrl'
      }
    },
    onEnter: function($document, $timeout) {
      $timeout(function() {
        var top = angular.element($document.find('#top'));
        var scrollContainer = angular.element(
          $document.find('md-content[du-scroll-container]'));
        scrollContainer.scrollToElementAnimated(top);
      });
    }
  };
  $stateProvider.state('shoppingCart', _.extend({}, commonStateConfig, {
    url: '/shopping-cart',
    resolve: {
      order: function() {
        return null;
      }
    }
  }));
  $stateProvider.state('restoreShoppingCart', _.extend({}, commonStateConfig, {
    url: '/shopping-cart/:id',
    resolve: {
      order: function($state, $stateParams, OrderResource) {
        var order = OrderResource.get({id: $stateParams.id});
        order.$promise.then(null, function(error) {
          if (error.status === 404) {
            $state.go('shoppingCart');
          }
        });
        return order;
      }
    }
  }));
});

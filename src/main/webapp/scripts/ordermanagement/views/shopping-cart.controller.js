/* global _ */
'use strict';

angular.module('metadatamanagementApp').controller('ShoppingCartController',
  function(PageTitleService, $state, ToolbarHeaderService,
           ShoppingCartService, $scope, StudyResource, DataSetSearchService,
           VariableSearchService, DataAcquisitionProjectReleasesResource, $q,
           OrderResource, LanguageService, SimpleMessageToastService, order) {

    PageTitleService.setPageTitle('shopping-cart.title');
    ToolbarHeaderService.updateToolbarHeader({
      'stateName': $state.current.name
    });
    var ctrl = this;
    var existingOrderId;
    var shoppingCartVersion;
    ctrl.studies = {};
    ctrl.releases = {};
    ctrl.counts = {};
    ctrl.initComplete = false;

    ctrl.init = function() {
      var promises = [];
      existingOrderId = ShoppingCartService.getOrderId();
      shoppingCartVersion = ShoppingCartService.getShoppingCartVersion();
      ctrl.products = ShoppingCartService.getProducts();
      ctrl.products.forEach(function(product) {
        var studyId = product.study.id;

        ctrl.studies[studyId] = {};
        ctrl.releases[product.dataAcquisitionProjectId] = {};
        promises.push(DataSetSearchService.countByMultiple({
          'studyId': studyId,
          'accessWays': product.accessWay
        }).then(function(result) {
          ctrl.counts[studyId + product.accessWay + product.version] =
            ctrl.counts[studyId + product.accessWay +
            product.version] || {};
          ctrl.counts[studyId + product.accessWay +
          product.version].dataSets = result.count;
        }));
        promises.push(VariableSearchService.countByMultiple({
          'studyId': studyId,
          'accessWays': product.accessWay
        }).then(function(result) {
          ctrl.counts[studyId + product.accessWay + product.version] =
            ctrl.counts[studyId + product.accessWay +
            product.version] || {};
          ctrl.counts[studyId + product.accessWay +
          product.version].variables = result.count;
        }));
      });
      _.forEach(ctrl.studies, function(study, studyId) { // jshint ignore:line
        promises.push(StudyResource.get({id: studyId}).$promise.then(
          function(study) {
            ctrl.studies[studyId] = study;
          }));
      });
      _.forEach(ctrl.releases,
        function(release, projectId) { // jshint ignore:line
          promises.push(DataAcquisitionProjectReleasesResource.get(
            {id: projectId}).$promise.then(function(releases) {
            if (releases.length > 0) {
              ctrl.releases[projectId] = releases[0];
            }
          }));
        });
      $q.all(promises).then(function() {
        ctrl.initComplete = true;
        // remove all product which are not available anymore
        ctrl.products.forEach(function(product) {
          if (ctrl.studies[product.study.studyId]
            .dataAvailability.en === 'Available') {
            return;
          } else {
            ShoppingCartService.remove(product);
          }
        });
      });
    };

    $scope.$on('shopping-cart-changed', function() {
      ctrl.products = ShoppingCartService.getProducts();
    });

    ctrl.getNumberOfVariables = function(product) {
      return ctrl.counts[product.study.id + product.accessWay + product.version]
        .variables;
    };

    ctrl.getNumberOfDataSets = function(product) {
      return ctrl.counts[product.study.id + product.accessWay + product.version]
        .dataSets;
    };

    ctrl.isCurrentVersion = function(product) {
      return ctrl.releases[product.dataAcquisitionProjectId].version ===
        product.version;
    };

    ctrl.deleteProduct = function(product) {
      ShoppingCartService.remove(product);
    };

    ctrl.order = function() {
      if ($scope.orderForm.$valid) {
        var order = {
          languageKey: LanguageService.getCurrentInstantly(),
          client: 'MDM',
          products: []
        };
        ctrl.products.forEach(function(product) {
          var completeProduct = {
            dataAcquisitionProjectId: product.dataAcquisitionProjectId,
            study: ctrl.studies[product.study.id],
            accessWay: product.accessWay,
            version: product.version
          };
          order.products.push(completeProduct);
        });

        var orderFn;
        var requestParams;

        if (existingOrderId) {
          order.version = shoppingCartVersion;
          orderFn = OrderResource.update;
          requestParams = {
            id: existingOrderId
          };
        } else {
          orderFn = OrderResource.save;
        }

        orderFn(requestParams, order).$promise.then(function() {
          ShoppingCartService.clear();
          ctrl.orderSaved = true;
        }).catch(function() {
          SimpleMessageToastService.openAlertMessageToast(
            'shopping-cart.toasts.error-on-saving-order');
        });
      } else {
        // ensure that all validation errors are visible
        angular.forEach($scope.orderForm.$error, function(field) {
          angular.forEach(field, function(errorField) {
            if (errorField) {
              errorField.$setTouched();
            }
          });
        });
        SimpleMessageToastService.openAlertMessageToast(
          'shopping-cart.toasts.order-has-validation-errors-toast');
      }
    };

    ctrl.clear = function() {
      ShoppingCartService.clear();
    };

    if (order) {
      order.$promise.then(function(order) {
        ShoppingCartService
          .initShoppingCartProducts(_.get(order, 'products', []), order.id,
            order.version);
        ctrl.init();
      });
    } else {
      ctrl.init();
    }
  });

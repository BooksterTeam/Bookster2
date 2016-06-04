(function () {
  'use strict';
  angular
    .module('bookster2App')
    .config(stateConfig);

  stateConfig.$inject = [ '$stateProvider' ];

  function stateConfig ($stateProvider) {
    $stateProvider.state('market', {
        parent: 'app',
        url: '/market',
        data: {
          authorities: [],
          pageTitle: 'markets.title'
        },
        views: {
          'content@': {
            templateUrl: 'app/market/market.html',
            controller: 'MarketController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          mainTranslatePartialLoader: [ '$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('market');
            return $translate.refresh();
          } ]
        }
      })
      .state('market-detail', {
        parent: 'market',
        url: '/{id}',
        data: {
          authorities: [],
          pageTitle: 'bookster2App.market.detail.title'
        },
        views: {
          'content@': {
            templateUrl: 'app/market/market-detail.html',
            controller: 'MarketDetailController',
            controllerAs: 'vm'
          }
        },
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('book');
            $translatePartialLoader.addPart('copy');
            return $translate.refresh();
          }],
          entity: ['$stateParams', 'Book', function($stateParams, Book) {
            return Book.get({id : $stateParams.id});
          }]
        }
      })
      .state('market.borrow', {
        parent: 'market-detail',
        url: '/borrow?{copyId}',
        data: {
          authorities: ['ROLE_USER']
        },
        onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
          $uibModal.open({
            templateUrl: 'app/market/market-dialog.html',
            controller: 'MarketDialogController',
            controllerAs: 'vm',
            backdrop: 'static',
            size: 'lg',
            resolve: {
              translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('book');
                $translatePartialLoader.addPart('copy');
                $translatePartialLoader.addPart('lending');
                $translatePartialLoader.addPart('lendingRequest');
                return $translate.refresh();
              }],
              entity: ['Copy','$log', function(Copy, $log) {
                $log.log($stateParams)
                var copi = Copy.get({id : $stateParams.copyId});
                var res = {
                  bookId: $stateParams.id,
                  copyId: $stateParams.copyId,
                  copi: copi
                }
                return res;
              }]
            }
          }).result.then(function() {
            $state.go('market-detail', null, { reload: true });
          }, function() {
            $state.go('market-detail');
          });
        }]
      });
  }
})();



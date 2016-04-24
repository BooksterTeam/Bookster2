(function () {
    'use strict';
    angular
        .module('bookster2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
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
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('market');
                        return $translate.refresh();
                    }]
                }
            });
    }
})();



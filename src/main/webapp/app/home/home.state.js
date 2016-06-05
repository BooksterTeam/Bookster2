(function() {
    'use strict';

    angular
        .module('bookster2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('home', {
            parent: 'app',
            url: '/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/home/home.html',
                    controller: 'HomeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('home');
                    $translatePartialLoader.addPart('lendingRequest');
                    $translatePartialLoader.addPart('lending');
                    $translatePartialLoader.addPart('copy');
                    $translatePartialLoader.addPart('requestStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        }).state('home.reject', {
            parent: 'home',
            url: 'reject?{id}',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/home/reject-dialog.html',
                    controller: 'RejectController',
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
                        entity: ['LendingRequest','$log', function(LendingRequest, $log) {
                            var lendingRequest = LendingRequest.get({id: $stateParams.id})
                            return lendingRequest;
                        }]
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: true });
                }, function() {
                    $state.go('home');
                });
            }]
        });
    }
})();

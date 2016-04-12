(function() {
    'use strict';

    angular
        .module('bookster2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lending-request', {
            parent: 'entity',
            url: '/lending-request?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookster2App.lendingRequest.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lending-request/lending-requests.html',
                    controller: 'LendingRequestController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lendingRequest');
                    $translatePartialLoader.addPart('requestStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lending-request-detail', {
            parent: 'entity',
            url: '/lending-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookster2App.lendingRequest.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lending-request/lending-request-detail.html',
                    controller: 'LendingRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lendingRequest');
                    $translatePartialLoader.addPart('requestStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LendingRequest', function($stateParams, LendingRequest) {
                    return LendingRequest.get({id : $stateParams.id});
                }]
            }
        })
        .state('lending-request.new', {
            parent: 'lending-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lending-request/lending-request-dialog.html',
                    controller: 'LendingRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lending-request', null, { reload: true });
                }, function() {
                    $state.go('lending-request');
                });
            }]
        })
        .state('lending-request.edit', {
            parent: 'lending-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lending-request/lending-request-dialog.html',
                    controller: 'LendingRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LendingRequest', function(LendingRequest) {
                            return LendingRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lending-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lending-request.delete', {
            parent: 'lending-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lending-request/lending-request-delete-dialog.html',
                    controller: 'LendingRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LendingRequest', function(LendingRequest) {
                            return LendingRequest.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lending-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('bookster2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('copy', {
            parent: 'entity',
            url: '/copy?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookster2App.copy.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/copy/copies.html',
                    controller: 'CopyController',
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
                    $translatePartialLoader.addPart('copy');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('copy-detail', {
            parent: 'entity',
            url: '/copy/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookster2App.copy.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/copy/copy-detail.html',
                    controller: 'CopyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('copy');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Copy', function($stateParams, Copy) {
                    return Copy.get({id : $stateParams.id});
                }]
            }
        })
        .state('copy.new', {
            parent: 'copy',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/copy/copy-dialog.html',
                    controller: 'CopyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                verified: false,
                                available: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('copy', null, { reload: true });
                }, function() {
                    $state.go('copy');
                });
            }]
        })
        .state('copy.edit', {
            parent: 'copy',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/copy/copy-dialog.html',
                    controller: 'CopyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Copy', function(Copy) {
                            return Copy.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('copy', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('copy.delete', {
            parent: 'copy',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/copy/copy-delete-dialog.html',
                    controller: 'CopyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Copy', function(Copy) {
                            return Copy.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('copy', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

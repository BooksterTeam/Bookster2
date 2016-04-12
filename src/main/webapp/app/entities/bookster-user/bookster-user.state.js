(function() {
    'use strict';

    angular
        .module('bookster2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bookster-user', {
            parent: 'entity',
            url: '/bookster-user?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookster2App.booksterUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bookster-user/bookster-users.html',
                    controller: 'BooksterUserController',
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
                    $translatePartialLoader.addPart('booksterUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bookster-user-detail', {
            parent: 'entity',
            url: '/bookster-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookster2App.booksterUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bookster-user/bookster-user-detail.html',
                    controller: 'BooksterUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('booksterUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BooksterUser', function($stateParams, BooksterUser) {
                    return BooksterUser.get({id : $stateParams.id});
                }]
            }
        })
        .state('bookster-user.new', {
            parent: 'bookster-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bookster-user/bookster-user-dialog.html',
                    controller: 'BooksterUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bookster-user', null, { reload: true });
                }, function() {
                    $state.go('bookster-user');
                });
            }]
        })
        .state('bookster-user.edit', {
            parent: 'bookster-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bookster-user/bookster-user-dialog.html',
                    controller: 'BooksterUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BooksterUser', function(BooksterUser) {
                            return BooksterUser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bookster-user', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bookster-user.delete', {
            parent: 'bookster-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bookster-user/bookster-user-delete-dialog.html',
                    controller: 'BooksterUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BooksterUser', function(BooksterUser) {
                            return BooksterUser.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('bookster-user', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

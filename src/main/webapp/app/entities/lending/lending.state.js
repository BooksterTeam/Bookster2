(function() {
    'use strict';

    angular
        .module('bookster2App')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lending', {
            parent: 'entity',
            url: '/lending',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookster2App.lending.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lending/lendings.html',
                    controller: 'LendingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lending');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lending-detail', {
            parent: 'entity',
            url: '/lending/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bookster2App.lending.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lending/lending-detail.html',
                    controller: 'LendingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lending');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lending', function($stateParams, Lending) {
                    return Lending.get({id : $stateParams.id});
                }]
            }
        })
        .state('lending.new', {
            parent: 'lending',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lending/lending-dialog.html',
                    controller: 'LendingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fromDate: null,
                                dueDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lending', null, { reload: true });
                }, function() {
                    $state.go('lending');
                });
            }]
        })
        .state('lending.edit', {
            parent: 'lending',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lending/lending-dialog.html',
                    controller: 'LendingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lending', function(Lending) {
                            return Lending.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lending', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lending.delete', {
            parent: 'lending',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lending/lending-delete-dialog.html',
                    controller: 'LendingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lending', function(Lending) {
                            return Lending.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lending', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

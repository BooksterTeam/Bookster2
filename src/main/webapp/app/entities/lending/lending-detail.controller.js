(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('LendingDetailController', LendingDetailController);

    LendingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Lending', 'BooksterUser', 'Copy'];

    function LendingDetailController($scope, $rootScope, $stateParams, entity, Lending, BooksterUser, Copy) {
        var vm = this;
        vm.lending = entity;
        vm.load = function (id) {
            Lending.get({id: id}, function(result) {
                vm.lending = result;
            });
        };
        var unsubscribe = $rootScope.$on('bookster2App:lendingUpdate', function(event, result) {
            vm.lending = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

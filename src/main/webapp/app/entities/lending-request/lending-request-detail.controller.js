(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('LendingRequestDetailController', LendingRequestDetailController);

    LendingRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'LendingRequest', 'Lending', 'BooksterUser'];

    function LendingRequestDetailController($scope, $rootScope, $stateParams, entity, LendingRequest, Lending, BooksterUser) {
        var vm = this;
        vm.lendingRequest = entity;
        vm.load = function (id) {
            LendingRequest.get({id: id}, function(result) {
                vm.lendingRequest = result;
            });
        };
        var unsubscribe = $rootScope.$on('bookster2App:lendingRequestUpdate', function(event, result) {
            vm.lendingRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('BooksterUserDetailController', BooksterUserDetailController);

    BooksterUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'BooksterUser', 'LendingRequest', 'Copy', 'Lending', 'User'];

    function BooksterUserDetailController($scope, $rootScope, $stateParams, entity, BooksterUser, LendingRequest, Copy, Lending, User) {
        var vm = this;
        vm.booksterUser = entity;
        vm.load = function (id) {
            BooksterUser.get({id: id}, function(result) {
                vm.booksterUser = result;
            });
        };
        var unsubscribe = $rootScope.$on('bookster2App:booksterUserUpdate', function(event, result) {
            vm.booksterUser = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

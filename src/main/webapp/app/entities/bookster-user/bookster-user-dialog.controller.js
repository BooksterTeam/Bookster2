(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('BooksterUserDialogController', BooksterUserDialogController);

    BooksterUserDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'BooksterUser', 'LendingRequest', 'Copy', 'Lending', 'User'];

    function BooksterUserDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, BooksterUser, LendingRequest, Copy, Lending, User) {
        var vm = this;
        vm.booksterUser = entity;
        vm.lendingRequests = LendingRequest.query();
        vm.copys = Copy.query();
        vm.lendings = Lending.query();
        vm.users = User.query();
        vm.load = function(id) {
            BooksterUser.get({id : id}, function(result) {
                vm.booksterUser = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bookster2App:booksterUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.booksterUser.id !== null) {
                BooksterUser.update(vm.booksterUser, onSaveSuccess, onSaveError);
            } else {
                BooksterUser.save(vm.booksterUser, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

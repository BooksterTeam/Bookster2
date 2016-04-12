(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('BooksterUserDialogController', BooksterUserDialogController);

    BooksterUserDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'BooksterUser', 'Copy', 'Lending', 'LendingRequest'];

    function BooksterUserDialogController ($scope, $stateParams, $uibModalInstance, entity, BooksterUser, Copy, Lending, LendingRequest) {
        var vm = this;
        vm.booksterUser = entity;
        vm.copys = Copy.query();
        vm.lendings = Lending.query();
        vm.lendingrequests = LendingRequest.query();
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

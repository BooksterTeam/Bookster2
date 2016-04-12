(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('CopyDialogController', CopyDialogController);

    CopyDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Copy', 'Book', 'Lending', 'BooksterUser'];

    function CopyDialogController ($scope, $stateParams, $uibModalInstance, entity, Copy, Book, Lending, BooksterUser) {
        var vm = this;
        vm.copy = entity;
        vm.books = Book.query();
        vm.lendings = Lending.query();
        vm.booksterusers = BooksterUser.query();
        vm.load = function(id) {
            Copy.get({id : id}, function(result) {
                vm.copy = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bookster2App:copyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.copy.id !== null) {
                Copy.update(vm.copy, onSaveSuccess, onSaveError);
            } else {
                Copy.save(vm.copy, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

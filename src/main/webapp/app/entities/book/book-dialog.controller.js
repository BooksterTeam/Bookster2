(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('BookDialogController', BookDialogController);

    BookDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Book', 'Author', 'Copy', 'Tag'];

    function BookDialogController ($scope, $stateParams, $uibModalInstance, entity, Book, Author, Copy, Tag) {
        var vm = this;
        vm.book = entity;
        vm.authors = Author.query();
        vm.copys = Copy.query();
        vm.tags = Tag.query();
        vm.load = function(id) {
            Book.get({id : id}, function(result) {
                vm.book = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bookster2App:bookUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.book.id !== null) {
                Book.update(vm.book, onSaveSuccess, onSaveError);
            } else {
                Book.save(vm.book, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

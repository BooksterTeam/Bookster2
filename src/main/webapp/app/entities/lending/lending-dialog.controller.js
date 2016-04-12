(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('LendingDialogController', LendingDialogController);

    LendingDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lending', 'LendingRequest', 'BooksterUser', 'Copy'];

    function LendingDialogController ($scope, $stateParams, $uibModalInstance, entity, Lending, LendingRequest, BooksterUser, Copy) {
        var vm = this;
        vm.lending = entity;
        vm.lendingrequests = LendingRequest.query();
        vm.booksterusers = BooksterUser.query();
        vm.copys = Copy.query();
        vm.load = function(id) {
            Lending.get({id : id}, function(result) {
                vm.lending = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bookster2App:lendingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.lending.id !== null) {
                Lending.update(vm.lending, onSaveSuccess, onSaveError);
            } else {
                Lending.save(vm.lending, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fromDate = false;
        vm.datePickerOpenStatus.dueDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();

(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('LendingDialogController', LendingDialogController);

    LendingDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Lending', 'BooksterUser', 'Copy'];

    function LendingDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, Lending, BooksterUser, Copy) {
        var vm = this;
        vm.lending = entity;
        vm.booksterusers = BooksterUser.query();
        vm.copys = Copy.query({filter: 'lending-is-null'});
        $q.all([vm.lending.$promise, vm.copys.$promise]).then(function() {
            if (!vm.lending.copy || !vm.lending.copy.id) {
                return $q.reject();
            }
            return Copy.get({id : vm.lending.copy.id}).$promise;
        }).then(function(copy) {
            vm.copys.push(copy);
        });
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

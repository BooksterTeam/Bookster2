(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('LendingRequestDialogController', LendingRequestDialogController);

    LendingRequestDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'LendingRequest', 'BooksterUser', 'Copy'];

    function LendingRequestDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, LendingRequest, BooksterUser, Copy) {
        var vm = this;
        vm.lendingRequest = entity;
        vm.booksterusers = BooksterUser.query();
        vm.copies = Copy.query({filter: 'lendingrequest-is-null'});
        $q.all([vm.lendingRequest.$promise, vm.copies.$promise]).then(function() {
            if (!vm.lendingRequest.copie || !vm.lendingRequest.copie.id) {
                return $q.reject();
            }
            return Copy.get({id : vm.lendingRequest.copie.id}).$promise;
        }).then(function(copie) {
            vm.copies.push(copie);
        });
        vm.load = function(id) {
            LendingRequest.get({id : id}, function(result) {
                vm.lendingRequest = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('bookster2App:lendingRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.lendingRequest.id !== null) {
                LendingRequest.update(vm.lendingRequest, onSaveSuccess, onSaveError);
            } else {
                LendingRequest.save(vm.lendingRequest, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.fromDate = false;
        vm.datePickerOpenStatus.dueDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();

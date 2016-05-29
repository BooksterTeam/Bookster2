(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('LendingRequestDialogController', LendingRequestDialogController);

    LendingRequestDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'LendingRequest', 'BooksterUser', 'Lending'];

    function LendingRequestDialogController ($scope, $stateParams, $uibModalInstance, $q, entity, LendingRequest, BooksterUser, Lending) {
        var vm = this;
        vm.lendingRequest = entity;
        vm.booksterusers = BooksterUser.query();
        vm.lendings = Lending.query({filter: 'lendingrequest-is-null'});
        $q.all([vm.lendingRequest.$promise, vm.lendings.$promise]).then(function() {
            if (!vm.lendingRequest.lending || !vm.lendingRequest.lending.id) {
                return $q.reject();
            }
            return Lending.get({id : vm.lendingRequest.lending.id}).$promise;
        }).then(function(lending) {
            vm.lendings.push(lending);
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
        vm.datePickerOpenStatus.date = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();

(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('MarketDialogController',MarketDialogController);

    MarketDialogController.$inject = ['$scope','$uibModalInstance', 'entity', '$log', 'Market', 'AlertService'];

    function MarketDialogController($scope, $uibModalInstance, entity, $log, Market, AlertService) {
        var vm = this;

        vm.lendingrequest = {};
        vm.bookId = entity.bookId
        vm.copi = entity.copi;;

        var onSaveSuccess = function (result) {
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.borrow = function () {
            vm.isSaving = true;
            vm.lendingrequest.copie = vm.copi
            Market.save(vm.lendingrequest, onSaveSuccess, onSaveError)
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fromDate = false;
        vm.datePickerOpenStatus.dueDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();

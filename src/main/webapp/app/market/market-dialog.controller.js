(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('MarketDialogController',MarketDialogController);

    MarketDialogController.$inject = ['$uibModalInstance', 'entity', 'LendingRequest', '$log'];

    function MarketDialogController($uibModalInstance, entity, LendingRequest, $log) {
        var vm = this;
        $log.log(entity)
        vm.bookId = entity.bookId
        vm.copi = entity.copi;

        $log.log(vm)
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.borrow = function (copyId) {
            $log.log(copyId)
            $log.log("Here")
            //$log.log(vm.book)
            /*Market.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });*/
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fromDate = false;
        vm.datePickerOpenStatus.dueDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();

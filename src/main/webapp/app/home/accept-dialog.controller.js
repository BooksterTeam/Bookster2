(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('AcceptController',AcceptController);

    AcceptController.$inject = ['$uibModalInstance', 'entity', 'AcceptLendingRequest', '$log'];

    function AcceptController($uibModalInstance, entity, AcceptLendingRequest, $log) {
        var vm = this;
        vm.lendingRequest = entity;
        $log.log(entity);
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmAccept = function (content) {
            $log.log(content)
            $log.log(vm.lendingRequest);
            AcceptLendingRequest.save(vm.lendingRequest,
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

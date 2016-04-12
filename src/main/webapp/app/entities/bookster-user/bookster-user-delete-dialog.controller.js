(function() {
    'use strict';

    angular
        .module('bookster2App')
        .controller('BooksterUserDeleteController',BooksterUserDeleteController);

    BooksterUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'BooksterUser'];

    function BooksterUserDeleteController($uibModalInstance, entity, BooksterUser) {
        var vm = this;
        vm.booksterUser = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            BooksterUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

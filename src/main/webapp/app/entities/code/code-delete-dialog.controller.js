(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('CodeDeleteController',CodeDeleteController);

    CodeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Code'];

    function CodeDeleteController($uibModalInstance, entity, Code) {
        var vm = this;
        vm.code = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Code.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

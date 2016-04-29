(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('ContentDeleteController',ContentDeleteController);

    ContentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Content'];

    function ContentDeleteController($uibModalInstance, entity, Content) {
        var vm = this;
        vm.content = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Content.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

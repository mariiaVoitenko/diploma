(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('SightseeingDeleteController',SightseeingDeleteController);

    SightseeingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sightseeing'];

    function SightseeingDeleteController($uibModalInstance, entity, Sightseeing) {
        var vm = this;
        vm.sightseeing = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Sightseeing.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

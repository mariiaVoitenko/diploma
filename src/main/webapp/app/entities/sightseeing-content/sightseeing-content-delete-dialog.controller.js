(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('Sightseeing_contentDeleteController',Sightseeing_contentDeleteController);

    Sightseeing_contentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sightseeing_content'];

    function Sightseeing_contentDeleteController($uibModalInstance, entity, Sightseeing_content) {
        var vm = this;
        vm.sightseeing_content = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Sightseeing_content.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();

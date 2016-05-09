(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('RegionDialogController', RegionDialogController);

    RegionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Region', 'Country'];

    function RegionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Region, Country) {
        var vm = this;
        vm.region = entity;
        vm.countries = Country.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('diplomaApp:regionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.region.id !== null) {
                Region.update(vm.region, onSaveSuccess, onSaveError);
            } else {
                Region.save(vm.region, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

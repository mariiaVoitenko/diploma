(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('SightseeingDialogController', SightseeingDialogController);

    SightseeingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sightseeing', 'Category', 'Region'];

    function SightseeingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sightseeing, Category, Region) {
        var vm = this;
        vm.sightseeing = entity;
        vm.categories = Category.query();
        vm.regions = Region.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('diplomaApp:sightseeingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.sightseeing.id !== null) {
                Sightseeing.update(vm.sightseeing, onSaveSuccess, onSaveError);
            } else {
                Sightseeing.save(vm.sightseeing, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

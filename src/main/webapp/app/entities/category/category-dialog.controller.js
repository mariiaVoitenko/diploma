(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('CategoryDialogController', CategoryDialogController);

    CategoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Category'];

    function CategoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Category) {
        var vm = this;
        vm.category = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('diplomaApp:categoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.category.id !== null) {
                Category.update(vm.category, onSaveSuccess, onSaveError);
            } else {
                Category.save(vm.category, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

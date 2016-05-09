(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('CodeDialogController', CodeDialogController);

    CodeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Code', 'Sightseeing'];

    function CodeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Code, Sightseeing) {
        var vm = this;
        vm.code = entity;
        vm.sightseeings = Sightseeing.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('diplomaApp:codeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.code.id !== null) {
                Code.update(vm.code, onSaveSuccess, onSaveError);
            } else {
                Code.save(vm.code, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

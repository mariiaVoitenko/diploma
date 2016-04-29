(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('ContentDialogController', ContentDialogController);

    ContentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Content'];

    function ContentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Content) {
        var vm = this;
        vm.content = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('diplomaApp:contentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.content.id !== null) {
                Content.update(vm.content, onSaveSuccess, onSaveError);
            } else {
                Content.save(vm.content, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

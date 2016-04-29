(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('Sightseeing_contentDialogController', Sightseeing_contentDialogController);

    Sightseeing_contentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sightseeing_content', 'Content', 'Sightseeing'];

    function Sightseeing_contentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sightseeing_content, Content, Sightseeing) {
        var vm = this;
        vm.sightseeing_content = entity;
        vm.contents = Content.query();
        vm.sightseeings = Sightseeing.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('diplomaApp:sightseeing_contentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.sightseeing_content.id !== null) {
                Sightseeing_content.update(vm.sightseeing_content, onSaveSuccess, onSaveError);
            } else {
                Sightseeing_content.save(vm.sightseeing_content, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

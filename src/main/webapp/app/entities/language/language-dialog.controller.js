(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('LanguageDialogController', LanguageDialogController);

    LanguageDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Language'];

    function LanguageDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Language) {
        var vm = this;
        vm.language = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('diplomaApp:languageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.language.id !== null) {
                Language.update(vm.language, onSaveSuccess, onSaveError);
            } else {
                Language.save(vm.language, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

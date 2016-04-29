(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('CountryDialogController', CountryDialogController);

    CountryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Country', 'Language'];

    function CountryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Country, Language) {
        var vm = this;
        vm.country = entity;
        vm.languages = Language.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('diplomaApp:countryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.country.id !== null) {
                Country.update(vm.country, onSaveSuccess, onSaveError);
            } else {
                Country.save(vm.country, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

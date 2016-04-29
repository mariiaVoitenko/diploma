(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('LanguageDetailController', LanguageDetailController);

    LanguageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Language'];

    function LanguageDetailController($scope, $rootScope, $stateParams, entity, Language) {
        var vm = this;
        vm.language = entity;
        
        var unsubscribe = $rootScope.$on('diplomaApp:languageUpdate', function(event, result) {
            vm.language = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('CountryDetailController', CountryDetailController);

    CountryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Country', 'Language'];

    function CountryDetailController($scope, $rootScope, $stateParams, entity, Country, Language) {
        var vm = this;
        vm.country = entity;
        
        var unsubscribe = $rootScope.$on('diplomaApp:countryUpdate', function(event, result) {
            vm.country = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

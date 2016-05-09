(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('RegionDetailController', RegionDetailController);

    RegionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Region', 'Country'];

    function RegionDetailController($scope, $rootScope, $stateParams, entity, Region, Country) {
        var vm = this;
        vm.region = entity;
        
        var unsubscribe = $rootScope.$on('diplomaApp:regionUpdate', function(event, result) {
            vm.region = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

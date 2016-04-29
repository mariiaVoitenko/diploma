(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('SightseeingDetailController', SightseeingDetailController);

    SightseeingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Sightseeing'];

    function SightseeingDetailController($scope, $rootScope, $stateParams, entity, Sightseeing) {
        var vm = this;
        vm.sightseeing = entity;
        
        var unsubscribe = $rootScope.$on('diplomaApp:sightseeingUpdate', function(event, result) {
            vm.sightseeing = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

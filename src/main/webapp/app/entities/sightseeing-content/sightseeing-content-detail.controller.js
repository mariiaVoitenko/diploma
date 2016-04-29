(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('Sightseeing_contentDetailController', Sightseeing_contentDetailController);

    Sightseeing_contentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Sightseeing_content', 'Content', 'Sightseeing'];

    function Sightseeing_contentDetailController($scope, $rootScope, $stateParams, entity, Sightseeing_content, Content, Sightseeing) {
        var vm = this;
        vm.sightseeing_content = entity;
        
        var unsubscribe = $rootScope.$on('diplomaApp:sightseeing_contentUpdate', function(event, result) {
            vm.sightseeing_content = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

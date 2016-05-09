(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('CodeDetailController', CodeDetailController);

    CodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Code', 'Sightseeing'];

    function CodeDetailController($scope, $rootScope, $stateParams, entity, Code, Sightseeing) {
        var vm = this;
        vm.code = entity;
        
        var unsubscribe = $rootScope.$on('diplomaApp:codeUpdate', function(event, result) {
            vm.code = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

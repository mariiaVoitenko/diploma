(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .controller('ContentDetailController', ContentDetailController);

    ContentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Content'];

    function ContentDetailController($scope, $rootScope, $stateParams, entity, Content) {
        var vm = this;
        vm.content = entity;
        
        var unsubscribe = $rootScope.$on('diplomaApp:contentUpdate', function(event, result) {
            vm.content = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();

(function() {
    'use strict';

    angular
        .module('diplomaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('code', {
            parent: 'entity',
            url: '/code?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Codes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/code/codes.html',
                    controller: 'CodeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('code-detail', {
            parent: 'entity',
            url: '/code/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Code'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/code/code-detail.html',
                    controller: 'CodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Code', function($stateParams, Code) {
                    return Code.get({id : $stateParams.id});
                }]
            }
        })
        .state('code.new', {
            parent: 'code',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/code/code-dialog.html',
                    controller: 'CodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                picture: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('code', null, { reload: true });
                }, function() {
                    $state.go('code');
                });
            }]
        })
        .state('code.edit', {
            parent: 'code',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/code/code-dialog.html',
                    controller: 'CodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Code', function(Code) {
                            return Code.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('code', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('code.delete', {
            parent: 'code',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/code/code-delete-dialog.html',
                    controller: 'CodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Code', function(Code) {
                            return Code.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('code', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

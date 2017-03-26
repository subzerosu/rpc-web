(function(angular) {
    'use strict';

    // routing
    angular.module('rpcApp').config(routerConfig);

    routerConfig.$inject = [ '$stateProvider', '$urlRouterProvider', '$breadcrumbProvider' ];
    function routerConfig($stateProvider, $urlRouterProvider, $breadcrumbProvider) {

        $breadcrumbProvider
                .setOptions({
                    prefixStateName : 'app',
                    includeAbstract : true,
                    templateUrl: 'app/layout/breadcrumbs.html'
                });

        // default state
        $urlRouterProvider.otherwise('/dashboard');

        var states = [ {
            name : 'app',
            abstract : true,
            templateUrl : 'app/layout/layout.html',
            ncyBreadcrumb : {
                label : 'RPC',
                skip : true
            }
        }, {
            name : 'app.dashboard',
            url : '/dashboard',
            component : 'dashboard',
            ncyBreadcrumb : {
                label : 'Главная',
            }
        }];

        states.forEach(function(state) {
            $stateProvider.state(state);
        });
    };

})(angular);
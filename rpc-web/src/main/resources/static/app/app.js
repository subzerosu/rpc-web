(function(angular) {
    'use strict';

    // 'ngTouch', 'ngAnimate', 'ngSanitize', 'ui.bootstrap'
    var app = angular.module('rpcApp', [ 'ui.router', 'ncy-angular-breadcrumb', 'rpcApp.tasks']);
    app.config(rpcConfig);
    app.run(rpcRun);

    // config, $logProvider
    rpcConfig.$inject = ['$httpProvider', '$locationProvider'];
    function rpcConfig($httpProvider, $locationProvider) {
        // #
        $locationProvider.hashPrefix('!');
       
        // angular csrf
        //$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    };

    // run
    rpcRun.$inject = ['$rootScope', '$state', '$stateParams'];
    function rpcRun($rootScope, $state, $stateParams) {
        $rootScope.$on('$stateChangeSuccess', function() {
            document.body.scrollTop = document.documentElement.scrollTop = 0;
        });
        
        $rootScope.$state = $state;
        return $rootScope.$stateParams = $stateParams;
    };

})(angular);
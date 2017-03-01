(function () {
    'use strict';

    //'ngTouch', 'ngAnimate', 'ngSanitize'
    var app = angular.module('rpcApp', ['ui.bootstrap']);
    app.config(['$httpProvider', '$locationProvider', '$logProvider',
        function ($httpProvider, $locationProvider, $logProvider) {
            // #
            $locationProvider.hashPrefix('!');

            //$locationProvider.html5Mode(true);

            // angular csrf
            $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

            // allow debug
            //$logProvider.debugEnabled(true);
        }]);

})();
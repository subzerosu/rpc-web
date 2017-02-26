(function () {
    'use strict';

    var app = angular.module('rpcApp', ['ngMaterial', 'ngMessages', 'ngMdIcons', 'ngRoute']);
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
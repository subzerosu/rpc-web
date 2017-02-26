(function (angular) {
    'use strict';
    var ctrl_name = 'mainCtrl';
    var url_pattern = '/';

    angular.module('rpcApp')
        .controller(ctrl_name, ["$scope", "$location", "$http", "$mdSidenav", "$log", MainController])
    .config(['$routeProvider', MainConfig]);

    function MainController($scope, $location, $http, $mdSidenav, $log) {
        var self = this;
        $scope.name = ctrl_name;

        $scope.currentNavItem = 'taskPage';
    };


    function MainConfig($routeProvider) {
        $routeProvider
            .when(url_pattern, {
                templateUrl: './app/pages/main.html',
                controller: ctrl_name
            }).otherwise({
            templateUrl: './app/pages/main.html'
        });
    };

})(angular);

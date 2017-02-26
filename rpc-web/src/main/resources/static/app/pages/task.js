(function (angular) {
    'use strict';
    var ctrl_name = 'taskCtrl';
    var url_pattern = '/tasks';

    angular.module('rpcApp')
        .controller(ctrl_name, ["$scope", "$log", "$routeParams", TaskController])
        .config(['$routeProvider', TaskConfig]);

    function TaskController($scope, $log, $routeParams) {
        var self = this;

        $scope.name = ctrl_name;
        $scope.params = $routeParams;
    };


    function TaskConfig($routeProvider) {
        $routeProvider
            .when(url_pattern, {
                templateUrl: './app/pages/task.html',
                controller: ctrl_name
            }).otherwise({
            templateUrl: './app/pages/main.html'
        });
    };

})(angular);

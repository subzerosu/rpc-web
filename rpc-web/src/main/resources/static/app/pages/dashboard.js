(function(angular) {
    'use strict';

    angular.module('rpcApp').component('dashboard', {
        templateUrl: 'app/pages/dashboard.html',
        controller : DashboardController
    });

    function DashboardController($rootScope, $http, $location) {
        var self = this;

        var authenticate = function(credentials, callback) {
            var headers = credentials ? {
                authorization : "Basic "
                + btoa(credentials.username + ":"
                    + credentials.password)
            } : {};

            $http.get('/api/rpc/user', {
                headers : headers
            }).then(function(response) {
                if (response.data.name) {
                    $rootScope.authenticated = true;
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback($rootScope.authenticated);
            }, function() {
                $rootScope.authenticated = false;
                callback && callback(false);
            });
        }

        authenticate();

        self.logout = function() {
            $http.post('logout', {}).finally(function() {
                $rootScope.authenticated = false;
                $location.path("/");
            });
        }
    };

})(angular);

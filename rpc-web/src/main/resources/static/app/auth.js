/**
 * Created by cane on 26.02.17.
 */
(function (angular) {
    'use strict';
    var ctrl_name = 'authCtrl';
    var url_pattern = '/';

    angular.module('rpcApp')
        .controller(ctrl_name, ["$scope", "$location", "$http", "$mdSidenav", "$log", AuthController]);

    function AuthController($scope, $location, $http, $log) {
        var self = this;

        $scope.name = ctrl_name;

        self.login = function () {
            $http
                .get('/login')
                .then(function successLogin(result) {
                    //$log.info("В систему вошли.");
                    console.log("В систему вошли.");
                    // ask user auth
                    $http
                        .get("/api/rpc/user")
                        .then(function success(result) {
                                // TODO
                                self.user = result.data.userAuthentication.details.name;
                                self.authenticated = true;
                                //$log.info("Текущий пользователь: " + self.user);
                                console.log("Текущий пользователь: ", self.user);
                            },
                            function error(result) {
                                self.user = "N/A";
                                self.authenticated = false;
                                //$log.error("Неудачная попытка войти в систему. " + result.data);
                                console.error("Неудачная попытка войти в систему. ", result.data);
                            });
                }, function failedLogin(result) {
                    //$log.error("В систему войти не удалось: {}.", result.data);
                    console.error("В систему войти не удалось: ", result.data);
                    // TODO message/page/401
                });
        };

        self.logout = function () {
            $http
                .post('/logout', {})
                .then(function success(result) {
                        //$log.info("Пользователь вышел из системы.");
                        console.log("Пользователь вышел из системы.");
                        self.authenticated = false;
                        $location.path(url_pattern);
                    },
                    function error(result) {
                        //$log.error("Неудачная попытка выйти из системы.");
                        console.error("Неудачная попытка выйти из системы.");
                        self.authenticated = false;
                        //$location.path(url_pattern);
                    });
        };
    };


})(angular);

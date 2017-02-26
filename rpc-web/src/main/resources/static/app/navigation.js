/**
 * Created by cane on 24.02.17.
 */
(function (angular) {
    'use strict';
    var ctrl_name = 'navCtrl';

    angular.module('rpcApp')
        .controller(ctrl_name, ["$scope", "$location", NavigationController]);

    function NavigationController($scope, $location) {
        var self = this;
        $scope.name = ctrl_name;

        self.menuItems = [
            {
                id: 1,
                label: 'Главная',
                href: '/',
                isActive: true
            },
            {
                id: 2,
                label: 'Задания',
                href: '/tasks',
                isActive: false
            }
        ];

        // TODO
        //

        $scope.menuItemClass = function (item) {
            var current = $location.path().substring(1);
            var menuClass = "";

            if (item.href === current) {
                item.isActive = true;
                menuClass = "md-no-ink md-primary";
            } else {
                item.isActive = false;
            }
            return menuClass;
        };


        $scope.menuItemClick = function (pos) {
            console.log("position: ", pos);
        };

    };

})(angular);


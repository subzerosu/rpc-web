(function (angular) {
    'use strict';

    angular.module('rpcApp').component('tasks', {
        bindings: {
            tasks: '<'
        },
        templateUrl: 'app/pages/tasks.html'
    });

})(angular);

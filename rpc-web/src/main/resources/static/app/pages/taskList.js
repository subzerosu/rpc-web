(function (angular) {
    'use strict';

    angular.module('rpcApp').component('taskList', {
        bindings: {
            tasks: '<'
        },
        templateUrl: 'app/pages/taskList.html'
    });

})(angular);

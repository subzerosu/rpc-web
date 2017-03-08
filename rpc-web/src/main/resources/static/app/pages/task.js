(function(angular) {
	'use strict';

	angular.module('rpcApp').component('task', {
        bindings: {
            task: '<'
        },
		templateUrl : 'app/pages/task.html'
	});

})(angular);

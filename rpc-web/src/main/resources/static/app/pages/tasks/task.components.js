(function(angular) {
	'use strict';

	angular.module('rpcApp.tasks')
	
	// task list
	.component('task.list', {
        bindings: {
            tasks: '<'
        },
        templateUrl: 'app/pages/tasks/task.list.html'
    })
	
    // task detail
	.component('task.detail', {
        bindings: {
            task: '<'
        },
		templateUrl : 'app/pages/tasks/task.detail.html'
	});

})(angular);

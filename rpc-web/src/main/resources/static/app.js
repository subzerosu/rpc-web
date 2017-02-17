(function () {
  'use strict';

  var app = angular.module('prcApp', ['ngMaterial', 'ngMessages', 'ngResource']);
  
  app.config(function($mdIconProvider) {
	  $mdIconProvider
	    .defaultIconSet('assets/mdi/mdi.svg')
	});
})();
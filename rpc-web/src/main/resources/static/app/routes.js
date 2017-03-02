angular
.module('rpcApp')
.config(['$stateProvider', '$urlRouterProvider', '$breadcrumbProvider', function($stateProvider, $urlRouterProvider, $breadcrumbProvider) {

  $urlRouterProvider.otherwise('/');

  $breadcrumbProvider.setOptions({
    prefixStateName: 'app.main',
    includeAbstract: true,
    template: '<li class="breadcrumb-item" ng-repeat="step in steps" ng-class="{active: $last}" ng-switch="$last || !!step.abstract"><a ng-switch-when="false" href="{{step.ncyBreadcrumbLink}}">{{step.ncyBreadcrumbLabel}}</a><span ng-switch-when="true">{{step.ncyBreadcrumbLabel}}</span></li>'
  });

  $stateProvider
  .state('app', {
    abstract: true,
    templateUrl: 'app/layouts/layout.html',
    //page title goes here
    ncyBreadcrumb: {
      label: 'Root',
      skip: true
    }
  })
  .state('app.main', {
    url: '/dashboard',
    templateUrl: 'app/pages/main.html',
    //page title goes here
    ncyBreadcrumb: {
      label: 'Home',
    },
    //page subtitle goes here
    params: { subtitle: 'Welcome' }
  });
}]);
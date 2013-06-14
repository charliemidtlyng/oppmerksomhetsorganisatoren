angular.module('ooRoutes', [])
    .config(function($routeProvider, $locationProvider) {
        $routeProvider

            .when('/', {
                templateUrl: 'assets/templates/personer.html',
                controller: 'PersonController',
                activetab: 'personer' // TODO: Refactor to directive
            })

            .when('/adresser', {
                templateUrl: 'assets/templates/adresser.html',
                controller: 'AdresseController',
                activetab: 'adresser'
            })

            .when('/personer', {
                templateUrl: 'assets/templates/personer.html',
                controller: 'PersonController',
                activetab: 'personer'
            })
            .when('/oppmerksomheter', {
                templateUrl: 'assets/templates/oppmerksomheter.html',
                controller: 'OppmerksomhetController',
                activetab: 'oppmerksomheter'
            })
            .when('/oppmerksomheter/:id', {
                templateUrl: 'assets/templates/oppmerksomhet.html',
                controller: 'EnkelOppmerksomhetController',
                activetab: 'oppmerksomheter'
            })
            .when('/personerPaaAdresse/:adresseId',{
                templateUrl: 'assets/templates/personerPaaAdresse.html',
                controller: 'PersonAdresseController',
                activetab: 'personer'
            })

            .when('/404', { templateUrl: 'assets/templates/404.html' })

        .otherwise( { redirectTo: '/404' });

        $locationProvider.html5Mode(false);
    })

    .run(function($rootScope, $location){
        $rootScope.menuActive = function(url, exactMatch){
            if (exactMatch){
                return $location.path() == url;
            }
            else {
                return $location.path().indexOf(url) == 0;
            }
        }
    });
;
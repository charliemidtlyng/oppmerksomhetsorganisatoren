angular.module('waRoutes', [])
    .config(function($routeProvider, $locationProvider) {
        $routeProvider

            .when('/', {
                templateUrl: 'assets/templates/today.html',
                controller: 'TodayController',
                activetab: 'today' // TODO: Refactor to directive
            })

            .when('/week', {
                templateUrl: 'assets/templates/week.html',
                controller: 'WeekController',
                activetab: 'week'
            })

            .when('/feedback', {
                templateUrl: 'assets/templates/feedback.html',
                controller: 'FeedbackController',
                activetab: 'feedback'
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
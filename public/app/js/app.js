'use strict';

angular.module('weatherApp', [
    'waRoutes',
    'waDirectives',
    'waWeatherService',
    'waSessionStore',
    'waSearchTermController',
    'waTodayController',
    'waWeekController',
    'waFeedbackController',
    'waAdresseController',
    'waPersonController',

    'ngResource'
]);
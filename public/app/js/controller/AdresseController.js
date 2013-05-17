'use strict';

angular.module('ooAdresseController', [])

    .controller('AdresseController', ['$scope', function ($scope) {

        $scope.submitAdresse = function() {
            $.post('/adresser', $scope.adresse).success(function(){
                fetchModel('adresseListe', '/adresser');
            });
        }

        $scope.slettAdresse = function(adresse) {
            $.post('/adresser/' + adresse.id + '/slett').success(function(result){
                fetchModel('adresseListe', '/adresser');
            });
        }

        function fetchModel(modelName, url) {
            $.get(url).success(function(result){
                $scope[modelName] = result;
                $scope.$apply()
            });
        }

        fetchModel('adresseListe', '/adresser');


    }])
;
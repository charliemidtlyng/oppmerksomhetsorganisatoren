'use strict';

angular.module('ooPersonController', [])

    .controller('PersonController', ['$scope', function ($scope) {

        $scope.adresseListe = [];
        $scope.personListe = [];
        $scope.person = {
            adresse: {}
        }

        $scope.submitPerson = function() {
            $scope.person.adresse.adresseId = $scope.person.adresse.id;
            $.post('/personer', JSON.stringify($scope.person)).success(function(){
                fetchModel('personListe', '/personer');
            }).error(function(error){
                alert(error);
            });
        }

        $scope.slettPerson = function(person) {
            $.post('/personer/' + person.id + '/slett').success(function(result){
                fetchModel('personListe', '/personer');
            });
        }

        function fetchModel(modelName, url) {
            $.get(url).success(function(result){
                $scope[modelName] = result;
                $scope.$apply()
            });
        }

        fetchModel('personListe', '/personer');
        fetchModel('adresseListe', '/adresser');

    }])
;
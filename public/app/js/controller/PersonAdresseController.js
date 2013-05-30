'use strict';

angular.module('ooPersonAdresseController', [])

    .controller('PersonAdresseController', ['$scope', '$routeParams', function ($scope, $routeParams) {

        $scope.personAdresse = {}

        $scope.personAdresse.adresse= $routeParams;
        $scope.personAdresse.personer= [];

//        console.log(person.adresse)

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
                $scope.personAdresse[modelName] = result;
                $scope.$apply()
            });
        }

        fetchModel('personer', '/personerPaaAdresse/'.concat($scope.personAdresse.adresse.adresseId));
        fetchModel('adresse', '/adresse/'.concat($scope.personAdresse.adresse.adresseId));

    }])
;
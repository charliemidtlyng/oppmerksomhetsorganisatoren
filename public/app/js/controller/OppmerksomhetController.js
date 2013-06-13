'use strict';

angular.module('ooOppmerksomhetController', [])

    .controller('OppmerksomhetController', ['$scope', function ($scope) {

        $scope.oppmerksomhet = {};
        $scope.involvertListe = [];
        $scope.hendelsestypeListe = ["Gave", "Gavekort", "Julekort", "Takkekort"];
        $scope.rolleListe = ["Mottaker", "Giver"];


        $scope.submitOppmerksomhet = function () {
            $scope.oppmerksomhet.fraType = $scope.oppmerksomhet.fra ? $scope.oppmerksomhet.fra.type : "";
            $scope.oppmerksomhet.tilType = $scope.oppmerksomhet.til ? $scope.oppmerksomhet.til.type : "";
            $scope.oppmerksomhet.verdi = $scope.oppmerksomhet.verdi ? $scope.oppmerksomhet.verdi.replace(/,/g, '.') : $scope.oppmerksomhet.verdi;
            console.log(JSON.stringify($scope.oppmerksomhet));
            $.post('/oppmerksomheter', JSON.stringify($scope.oppmerksomhet)).success(function () {
                alert(ok);
            }).error(function (error) {
                    alert(error);
                });
        }

        $scope.slettOppmerksomhet = function (oppmerksomhet) {
            $.post('/oppmerksomheter/' + oppmerksomhet.id + '/slett').success(function (result) {
//                fetchModel('personListe', '/personer');
                alert("ok");
            });
        }

        $.get("/personer").success(function (result) {
            var personer = _.map(result, function (person) {
                return {id: person.id, navn: person.navn, type: 'Person' };
            });
            $scope.involvertListe = _.union($scope.involvertListe, personer);
            $scope.$apply();
        });
        $.get("/adresser").success(function (result) {
            var adresser = _.map(result, function (adresse) {
                return {id: adresse.id, navn: adresse.familienavn.concat(" - ").concat(adresse.adressenavn), type: 'Familie' };
            });
            $scope.involvertListe = _.union($scope.involvertListe, adresser);
            $scope.$apply();
        });

    }])
;
angular.module('ooFilters', [])
    .filter('utledInvolvert', function () {
        return function (involvert, involvertType) {
            if(involvertType === 'Familie') {
                return involvert.familienavn;
            } else if( involvertType === 'Person') {
                return involvert.navn;
            }
            return '';
        };
    })
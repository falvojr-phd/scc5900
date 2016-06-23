/**
 * Exercício 1d (locais de corte):
 *
 * OPT(j) = 0                   se S[1..j] é palíndromo
 * OPT(j) = min { OPT(i) + 1 }  se S[i + 1..j] é palíndromo (0 < i < j)
 *
 * @author falvojr
 * @since 22/06/16.
 */

require('./utils');

var ex01b = require('./01b')
  , word = ex01b.word
  , memoizationVector = ex01b.memorizationVector;

(function () {
  'use strict';
  cutsLocations();
})();

/**
 * TODO: Backtracking!? on memorizationVector routes with 3 cuts, returning the first.
 */
function cutsLocations() {

}
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

var ex01b = require('./01b');

(function () {
  'use strict';
  console.log(getCutLocations(ex01b.memorization));
})();

/**
 * TODO: Pensar em uma estratégia adequada para recuperar os locais de corte do vetor de memoizacão
 * (em ultimo caso usar Backtracking).
 */
function getCutLocations(memorization) {

}
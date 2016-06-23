/**
 * Exercício 2c (substituicoes):
 *
 * OPT(j) = min {
 *    OPT(j-1)                    se S[j] minusculo
 *    OPT(j-1) + 1                se S[j] maiusculo
 *    #minusculos entre 1 e n-1   se S[j] maiusculo
 * }
 *
 * @author falvojr
 * @since 22/06/16.
 */

require('./utils');

var ex02b = require('./02b');

(function () {
  'use strict';
  console.log(getSubstitutions(ex02b.memorizationVector));
})();

function getSubstitutions(memorizationVector) {
  var substitutions = [];
  var memoizationMaxIndex = memorizationVector.length - 1;
  for (var i = 0; i < memoizationMaxIndex; i++) {
    var nextIndex = i + 1;
    if (memorizationVector[i] != memorizationVector[nextIndex]) {
      // Caso a diferenca seja nos ultimos dois elementos, o próximo indice é utilizado:
      substitutions.push(nextIndex == memoizationMaxIndex ? nextIndex : i);
    }
  }
  return substitutions;
}
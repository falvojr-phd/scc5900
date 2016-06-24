/**
 * Exercício 4d (sequencia de compras, vendas e resfriamentos):
 *
 *
 * @author falvojr
 * @since 23/06/16.
 */

require('./utils');

var ex04bc = require('./04bc');

(function () {
  'use strict';
  console.log(getSequence(ex04bc.memoization));
})();

/**
 * TODO: Pensar em uma estratégia adequada para recuperar a sequencia de compras, vendas e resfriamentos
 * usando o vetor/matriz de memoizacão.
 */
function getSequence(memorization) {
  var sequences = [];
  for (var i = 0; i < memorization.length; i++) {
    var nextIndex = i + 1;
    if (memorization[i] < memorization[nextIndex]) {

    } else if (memorization[i] > memorization[nextIndex]) {

    } else {

    }
  }
  return sequences;
}
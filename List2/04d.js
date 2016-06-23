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
  console.log(getSequence(ex04bc.memorizationVector));
})();

/**
 * TODO: Pensar em uma estratégia adequada para recuperar a sequencia de compras, vendas e resfriamentos
 * usando o vetor/matriz de memoizacão.
 */
function getSequence(memorizationVector) {
  var sequences = [];
  for (var i = 0; i < memorizationVector.length; i++) {
    var nextIndex = i + 1;
    if (memorizationVector[i] < memorizationVector[nextIndex]) {

    } else if (memorizationVector[i] > memorizationVector[nextIndex]) {

    } else {

    }
  }
  return sequences;
}
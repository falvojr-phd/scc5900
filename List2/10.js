/**
 * Exercício 10 (potenciacao em O(log n)).
 *
 * T(n) = 1                      se n == 1 (caso base)
 * T(n) = T([n/2]) + T([n/2])?   demais casos
 *
 * @author falvojr
 * @since 23/06/16.
 */

(function () {
  'use strict';
  console.log(exp(2, 3));
  // { inversions: [ [ 4, 1 ] ], sortedList: [ 2, 1, 4, 3, 5 ] }
})();

function exp(a, n) {
  var result, aux;
  if (n == 0) {
    result = 1;
  } else if (n % 2 == 0) {
    aux = exp(a, n / 2);
    result = aux * aux;
  } else {
    aux = exp(a, n >> 1);
    result = aux * aux * a;
  }
  return result;
}
/**
 * Exercício 6b.
 *
 * T(n) <= 0          se n < 2 (caso base)
 * T(n) <= T([n/2])   demais casos
 *
 * @author falvojr
 * @since 23/06/16.
 */

(function () {
  'use strict';
  console.log(t([0, 1, 2, 5, 11, 7, 4, 3]));
  // 11
})();

function t(elements) {
  var result;
  if (elements.length < 0) {
    result = undefined;
  } else if (elements.length == 1) {
    result = elements[0];
  } else if (elements.length == 2) {
    result = elements[elements[0] > elements[1] ? 0 : 1];
  } else {
    var m = elements.length >> 1
      , middle = elements[m]
      , left = elements[m - 1]
      , right = elements[m + 1];

    if (middle > left && middle > right) {
      result = middle;
    } else if (middle > left && middle < right) {
      result = t(elements.slice(m));
    } else {
      result = t(elements.slice(0, m));
    }
  }
  return result;
}
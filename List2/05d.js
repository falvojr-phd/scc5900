/**
 * Exercício 5d (subsequencia mais longa).
 *
 * OPT(i, j) = OPT(i-1, j-1) + 1                      se S[i] == S[j]
 * OPT(i, j) = min { OPT(i-1, j), OPT(i, j - 1) }     se S[i] != S[j]
 *
 * @author falvojr
 * @since 23/06/16.
 */

var ex05c = require('./05c');

(function () {
  'use strict';
  console.log(greaterSubSequenceBasedOnY(ex05c.memoization));
  // [ 8, 6, 4, 3, 2, 1 ]
  //
  // [ 0,   1,   2,   3,   4,   5,   6,   7,   8,   9 ]
  // [  , 'B', 'E', 'G', 'C', 'F', 'E', 'U', 'B', 'K'];
})();

function greaterSubSequenceBasedOnY(memorization) {
  var i = memorization.length - 1;
  var j = memorization[0].length - 1;
  var subSequence = [];
  while (i > 0 && j > 0) {
    if (memorization[i][j] == memorization[i - 1][j - 1] + 1) {
      i--;
      subSequence.push(j--);
    } else if (memorization[i - 1][j] > memorization[i][j - 1]) {
      i--;
    } else {
      j--;
    }
  }
  return subSequence;
}
/**
 * Exercício 1b (recursivo):
 *
 * OPT(j) = 0                   se S[1..j] é palíndromo
 * OPT(j) = min { OPT(i) + 1 }  se S[i + 1..j] é palíndromo (0 < i < j)
 *
 * @author falvojr
 * @since 22/06/16.
 */

require('./utils');

var word = 'ababbbabbababa'
  , memoizationVector = [];

(function () {
  'use strict';
  // Entire string test 'ababbbabbababa':
  console.log(opt(word, 14));
  // Test 'ababbbabba':
  console.log(opt(word, 10));
  // Test 'aba':
  console.log(opt(word, 3));
})();

function opt(word, j) {
  if (memoizationVector[j] == undefined) {
    var checkedWord = word.substring(0, j);
    if (checkedWord.isPalindrome()) {
      memoizationVector[j] = 0;
    } else {
      var i, opts = [];
      for (i = 1; i < j; i++) {
        checkedWord = word.substring(i, j);
        if (checkedWord.isPalindrome()) {
          opts.push(opt(word, i) + 1);
        }
      }
      memoizationVector[j] = opts.min();
    }
  }
  return memoizationVector[j];
}

module.exports.memorizationVector = memoizationVector;
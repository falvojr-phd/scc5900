/**
 * Exercício 1c (iterativo):
 *
 * OPT(j) = 0                   se S[1..j] é palíndromo
 * OPT(j) = min { OPT(i) + 1 }  se S[i + 1..j] é palíndromo (0 < i < j)
 *
 * @author falvojr
 * @since 22/06/16.
 */

require('./utils');

var memoization = [];

(function () {
  'use strict';
  console.log(opt('ababbbabbababa'));
  // 3
  // [  , a, b, a, b, b, b, a, b, b,  a,  b,  a,  b,  a ]
  // [  , 0, 1, 0, 1, 1, 1, 2, 1, 2,  2,  2,  3,  2,  3 ]
  // [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 ]
})();

function opt(word) {
  if (!memoization[word.length]) {
    if (word.isPalindrome()) {
      memoization[word.length] = 0;
    } else {
      for (var j = 1; j <= word.length; j++) {
        var opts = [];
        var checkedWord = word.substring(0, j);
        if (checkedWord.isPalindrome()) {
          memoization[j] = 0;
        } else {
          for (var i = 1; i < j; i++) {
            checkedWord = word.substring(i, j);
            if (checkedWord.isPalindrome()) {
              opts.push(memoization[i] + 1);
            }
          }
          memoization[j] = opts.min();
        }
      }
    }
  }
  return memoization[word.length];
}

module.exports.memoization = memoization;
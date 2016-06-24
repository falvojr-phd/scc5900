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
  , memoization = [];

(function () {
  'use strict';
  console.log(opt(word, 14));
  // 3
  // [  , a, b, a, b, b, b, a, b, b,  a,  b,  a,  b,  a ]
  // [  , 0, 1, 0, 1, 1, 1, 2, 1, 2,  2,  2,  3,  2,  3 ]
  // [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 ]

  console.log(opt(word, 10));
  // 2
  // [  , a, b, a, b, b, b, a, b, b,  a ]
  // [  , 0, 1, 0, 1, 1, 1, 2, 1, 2,  2 ]
  // [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ]

  console.log(opt(word, 3));
  // 0
  // [  , a, b, a ]
  // [  , 0, 1, 0 ]
  // [ 0, 1, 2, 3 ]
})();

function opt(word, j) {
  if (!memoization[j]) {
    var checkedWord = word.substring(0, j);
    if (checkedWord.isPalindrome()) {
      memoization[j] = 0;
    } else {
      var i, opts = [];
      for (i = 1; i < j; i++) {
        checkedWord = word.substring(i, j);
        if (checkedWord.isPalindrome()) {
          opts.push(opt(word, i) + 1);
        }
      }
      memoization[j] = opts.min();
    }
  }
  return memoization[j];
}

module.exports.memoization = memoization;
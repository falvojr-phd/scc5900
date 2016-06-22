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

var memorizationVector = [];

(function () {
  'use strict';
  var word = 'ababbbabbababa';
  console.log(opt(word, 14));
  console.log(opt(word, 13));
  console.log(opt(word, 12));
  console.log(opt(word, 11));
  console.log(opt(word, 10));
  console.log(opt(word, 9));
  console.log(opt(word, 8));
  console.log(opt(word, 7));
  console.log(opt(word, 6));
  console.log(opt(word, 5));
  console.log(opt(word, 4));
  console.log(opt(word, 3));
  console.log(opt(word, 2));
  console.log(opt(word, 1));
})();

function opt(word, j) {
  if (memorizationVector[j] == undefined) {
    var checkedWord = word.substring(0, j);
    if (checkedWord.isPalindrome() || j < 1) {
      memorizationVector[j] = 0;
    } else {
      var i, opts = [];
      for (i = 1; i < j; i++) {
        checkedWord = word.substring(i, j);
        if (checkedWord.isPalindrome()) {
          opts.push(opt(word, i) + 1);
        }
      }
      memorizationVector[j] = opts.min();
    }
  }
  return memorizationVector[j];
}

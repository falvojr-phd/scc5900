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

var memorizationVector = [];

(function () {
  'use strict';
  console.log(opt('ababbbabbababa'));
  console.log(opt('ababbbabba'));
  console.log(opt('aba'));
})();

function opt(word) {
  if (memorizationVector[word.length] == undefined) {
    if (word.isPalindrome()) {
      memorizationVector[word.length] = 0;
    } else {
      for (var j = 1; j <= word.length; j++) {
        var opts = [];
        var checkedWord = word.substring(0, j);
        if (checkedWord.isPalindrome()) {
          memorizationVector[j] = 0;
        } else {
          for (var i = 1; i < j; i++) {
            checkedWord = word.substring(i, j);
            if (checkedWord.isPalindrome()) {
              opts.push(memorizationVector[i] + 1);
            }
          }
          memorizationVector[j] = opts.min();
        }
      }
    }
  }
  return memorizationVector[word.length];
}

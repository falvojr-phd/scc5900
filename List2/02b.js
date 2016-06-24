/**
 * Exercício 2b (numero minimo de substituicoes):
 *
 * OPT(j) = min {   
 *    OPT(j-1)                    se S[j] minusculo
 *    OPT(j-1) + 1                se S[j] maiusculo
 *    #minusculos entre 1 e n-1   se S[j] maiusculo
 * }
 *
 * @author falvojr
 * @since 22/06/16.
 */

require('./utils');

var memoization = [];

(function () {
  'use strict';
  console.log(opt('AaAaAAAaAAAaAAaaaaaaA'));
  // 5
  // [ A, a, A, a, A, A, A, a, A, A,  A,  a,  A,  A,  a,  a,  a,  a,  a,  a,  A,]
  // [ 0, 0, 1, 1, 2, 2, 2, 2, 3, 3,  3,  3,  4,  4,  4,  4,  4,  4,  4,  4,  5 ]
  // [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 ]
})();


function opt(word) {
  var memoizationIndex = word.length - 1;
  if (!memoization[memoizationIndex]) {
    if (word.isUpperCase() || word.isLowerCase()) {
      memoization[memoizationIndex] = 0;
    } else {
      memoization[0] = 0;
      for (var j = 1; j < word.length; j++) {
        var previewValue = memoization[j - 1];
        if (word.charAt(j).isLowerCase()) {
          memoization[j] = previewValue;
        } else if (word.charAt(j).isUpperCase()){
          var lowersBetweenUppers = word.substring(1, j).countLowers();
          memoization[j] = Math.min(previewValue + 1, lowersBetweenUppers);
        }
      }
    }
  }
  return memoization[memoizationIndex];
}

module.exports.memorization = memoization;
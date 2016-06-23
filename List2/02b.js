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

var memoizationVector = [];

(function () {
  'use strict';
  console.log(opt('AaAaAAAaAAAaAAaaaaaaA'));
  // A  a  A  a  A  A  A  a  A  A  A  a  A  A  a  a  a  a  a  a  A
  // 0  0  1  1  2  2  2  2  3  3  3  3  4  4  4  4  4  4  4  4  5
})();


function opt(word) {
  var memoizationIndex = word.length - 1;
  if (memoizationVector[memoizationIndex] == undefined) {
    if (word.isUpperCase() || word.isLowerCase()) {
      memoizationVector[memoizationIndex] = 0;
    } else {
      memoizationVector[0] = 0;
      for (var j = 1; j < word.length; j++) {
        var previewValue = memoizationVector[j - 1];
        if (word.charAt(j).isLowerCase()) {
          memoizationVector[j] = previewValue;
        } else if (word.charAt(j).isUpperCase()){
          var lowersBetweenUppers = word.substring(1, j).countLowers();
          memoizationVector[j] = Math.min(previewValue + 1, lowersBetweenUppers);
        }
      }
    }
  }
  return memoizationVector[memoizationIndex];
}

module.exports.memorizationVector = memoizationVector;
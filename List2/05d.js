/**
 * Exercício 5c (subsequencia mais longa).
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
  console.log(greaterSubSequence(ex05c.memorization));
})();

function greaterSubSequence(memorization) {

}
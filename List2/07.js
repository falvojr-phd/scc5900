/**
 * Exercício 7 (contagem de inversões signficantes).
 *
 * @author falvojr
 * @since 23/06/16.
 */

(function () {
  'use strict';
  console.log(sortAndCount([2, 4, 1, 3, 5]));
  // { inversions: [ [ 4, 1 ] ], sortedList: [ 2, 1, 4, 3, 5 ] }
})();

function sortAndCount(list) {
  var result = {};
  // verifica se o problema é pequeno/simples e o resolve:
  if (list.length < 2) {
    result.inversions = [];
    result.sortedList = list;
  } else {
    // decompõem o problema em partes menores:
    var m = list.length >> 1;
    var resultA = sortAndCount(list.slice(0, m));
    var resultB = sortAndCount(list.slice(m));
    var resultMerge = mergeAndCount(resultA.sortedList, resultB.sortedList);
    // combina o resltado dos problemas menores:
    result.inversions = [].concat(resultA.inversions, resultB.inversions, resultMerge.inversions);
    result.sortedList = resultMerge.sortedList;
  }
  return result;
}

function mergeAndCount(a, b) {
  var result = {
    inversions: [],
    sortedList: []
  };
  
  while (a.length && b.length) {
    // Inversão significante: ai > 2aj
    if (a[0] > (2 * b[0])) {
      a.forEach(function (x) {
        result.inversions.push([x, b[0]])
      });
      result.sortedList.push(b.shift());
    } else {
      result.sortedList.push(a.shift());
    }
  }
  result.sortedList = result.sortedList.concat(a, b);
  return result;
}
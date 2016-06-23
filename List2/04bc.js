/**
 * Exercício 4b (vetor/matriz de memoizacão) e 4c (lucro máximo):
 *
 * OPTbuy(i)  = max { OPTsell(i-2) – prices[i],  OPTbuy(i-1) }   (0 <= i < n)
 * OPTsell(i) = max {  OPTbuy(i-1) + prices[i], OPTsell(i-1) }   (0 <= i < n)
 *
 * @author falvojr
 * @since 23/06/16.
 */

require('./utils');

var memoizationVector = [];

(function () {
  'use strict';
  console.log(opt([1, 2, 3, 0, 2]));
  console.log(memoizationVector);
})();

function opt(prices) {
  var memoizationIndex = prices.length - 1;
  if (memoizationVector[memoizationIndex] == undefined) {
    var sell = 0
      , prevSell = 0
      , buy = -Infinity
      , prevBuy;
    for (var i = 0; i < prices.length; i++) {
      var price = prices[i];
      prevBuy = buy;
      buy = Math.max(prevSell - price, prevBuy);
      prevSell = sell;
      sell = Math.max(prevBuy + price, prevSell);
      memoizationVector[i] = sell;
    }
  }
  return memoizationVector[memoizationIndex];
}

module.exports.memorizationVector = memoizationVector;
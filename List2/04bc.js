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

var memoization = [];
memoization[0] = []; // buy
memoization[1] = []; // sell

(function () {
  'use strict';
  console.log(opt([1, 2, 3, 0, 2]));
  // 3
  // [ -1, -1, -1, 1, 1 ] (buy)
  // [  0,  1,  2, 2, 3 ] (sell)
})();

function opt(prices) {
  var memoizationIndex = prices.length - 1;
  if (!memoization[1][memoizationIndex]) {
    var sell = 0
      , prevSell = 0
      , buy = -Infinity
      , prevBuy;
    for (var i = 0; i < prices.length; i++) {
      var price = prices[i];
      prevBuy = buy;
      buy = Math.max(prevSell - price, prevBuy);
      memoization[0][i] = buy;
      prevSell = sell;
      sell = Math.max(prevBuy + price, prevSell);
      memoization[1][i] = sell;
    }
  }
  return memoization[1][memoizationIndex];
}

module.exports.memorization = memoization;
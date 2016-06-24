/**
 * Exercício 5c (comprimento da subsequencia mais longa).
 *
 * OPT(i, j) = OPT(i-1, j-1) + 1                      se S[i] == S[j]
 * OPT(i, j) = min { OPT(i-1, j), OPT(i, j - 1) }     se S[i] != S[j]
 *
 * @author falvojr
 * @since 23/06/16.
 */

var memoization = [];

(function () {
  'use strict';
  var x = ['A', 'C', 'B', 'D', 'E', 'G', 'C', 'E', 'D', 'B', 'G'];
  var y = ['B', 'E', 'G', 'C', 'F', 'E', 'U', 'B', 'K'];
  console.log(opt(x, y));
  /*
   opt(y, x):
   [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ],
   [ 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1 ],
   [ 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2 ],
   [ 0, 0, 0, 1, 1, 2, 3, 3, 3, 3, 3, 3 ],
   [ 0, 0, 1, 1, 1, 2, 3, 4, 4, 4, 4, 4 ],
   [ 0, 0, 1, 1, 1, 2, 3, 4, 4, 4, 4, 4 ],
   [ 0, 0, 1, 1, 1, 2, 3, 4, 5, 5, 5, 5 ],
   [ 0, 0, 1, 1, 1, 2, 3, 4, 5, 5, 5, 5 ],
   [ 0, 0, 1, 2, 2, 2, 3, 4, 5, 5, 6, 6 ],
   [ 0, 0, 1, 2, 2, 2, 3, 4, 5, 5, 6, 6 ]

   opt(x, y):
   [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ],
   [ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 ],
   [ 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 ],
   [ 0, 1, 1, 1, 1, 1, 1, 1, 2, 2 ],
   [ 0, 1, 1, 1, 1, 1, 1, 1, 2, 2 ],
   [ 0, 1, 2, 2, 2, 2, 2, 2, 2, 2 ],
   [ 0, 1, 2, 3, 3, 3, 3, 3, 3, 3 ],
   [ 0, 1, 2, 3, 4, 4, 4, 4, 4, 4 ],
   [ 0, 1, 2, 3, 4, 4, 5, 5, 5, 5 ],
   [ 0, 1, 2, 3, 4, 4, 5, 5, 5, 5 ],
   [ 0, 1, 2, 3, 4, 4, 5, 5, 6, 6 ],
   [ 0, 1, 2, 3, 4, 4, 5, 5, 6, 6 ]
   */
})();

function opt(x, y) {
  if (!memoization[x.length]) {
    var n = x.length
      , m = y.length;

    // Cria uma matriz vazia:
    for (var i = 0; i <= n; i++) {
      memoization[i] = [];
      for (var j = 0; j <= m; j++) {
        memoization[i][j] = 0;
      }
    }

    // Preenche a matriz de memoização:
    for (i = 1; i <= n; i++) {
      for (j = 1; j <= m; j++) {
        if (x[i - 1] == y[j - 1]) {
          memoization[i][j] = memoization[i - 1][j - 1] + 1;
        } else {
          memoization[i][j] = Math.max(memoization[i - 1][j], memoization[i][j - 1]);
        }
      }
    }
  }
  return memoization[x.length][y.length];
}

module.exports.memorization = memoization;
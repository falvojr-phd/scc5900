/**
 * Created by falvojr on 22/06/16.
 */

String.prototype.isPalindrome = function isPalindrome() {
  return this == this.split('').reverse().join('');
};

Array.prototype.max = function() {
  return Math.max.apply(null, this);
};

Array.prototype.min = function() {
  return Math.min.apply(null, this);
};
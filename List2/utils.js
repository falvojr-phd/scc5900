/**
 * Created by falvojr on 22/06/16.
 */

String.prototype.isPalindrome = function () {
  return this == this.split('').reverse().join('');
};

Array.prototype.max = function() {
  return Math.max.apply(null, this);
};

Array.prototype.min = function() {
  return Math.min.apply(null, this);
};

String.prototype.isUpperCase = function () {
  return this == this.toUpperCase();
};

String.prototype.isLowerCase = function () {
  return this == this.toLowerCase();
};

String.prototype.countUppers = function () {
  return this.length - this.replace(/[A-Z]/g, '').length;
};

String.prototype.countLowers = function () {
  return this.length - this.replace(/[a-z]/g, '').length;
};

Array.prototype.last = function() {
  return this[this.lastIndex()];
};

Array.prototype.lastIndex = function() {
  return this.length - 1;
};
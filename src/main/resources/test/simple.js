var a = 4 + 2 + 3;
var b = 7;
var c = "aa";
var d = null;
var e = undefined;
var f = true;

b = a + b;

if (a > 2) {
    b = 7;
    console.log("ahoj");
}

while(a > 10) {
    b = a + b;
    b = b + 1;
}
b = b + 30;

console.log(b);
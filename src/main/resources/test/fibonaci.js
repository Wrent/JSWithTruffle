var i = 2;
var fib = []; // Initialize array!

fib[0] = 0;
fib[1] = 1;
while (i < 30)
{
    fib[i] = fib[i-2] + fib[i-1];
    console.log(fib[i]);
    i = i + 1;
}
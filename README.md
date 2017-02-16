# JSWithTruffle
JavaScript runtime implementation (partial) in Java using Truffle.

## Language description
Application implements subset of [Ecmascript](http://www.ecma-international.org/publications/standards/Ecma-262.htm), specifically:
 - variables - integer, string and boolean types
 - if branches
 - while loops
 - arrays accessible via [] operation
 - null, undefined
 - console output via console.log()
 
## Application example
Aplication requires GRAAL VM.

 1. Build the app using `mvn package`
 2. Run the app  `./js bubbleSort.js` or `./js fibonaci.js`
 3. Examine the output in the console

These two applications are pretty familiar to any computer scientist. First called `bubbleSort.js` performs simple sorting on a given array (using, of course, Bubble Sort algorithm), second one called `fibonaci.js` counts Fibonaci sequence.
 
Bubble Sort should output this:

`JSArray{list=[3, 9, 34, 198, 200, 203, 746, 764, 984]}`

Fibonaci should output this:
 
`1
 2
 3
 5
 8
 13
 21
 34
 55
 89
 144
 233
 377
 610
 987
 1597
 2584
 4181
 6765
 10946
 17711
 28657
 46368
 75025
 121393
 196418
 317811
 514229`
 
 
### How to run your own code
Insert your javascript file in  `/src/main/resources/test/`, build the app and run it with the correct filename.

## Unit tests
Basic unit tests can be found in test folder in file `JavascriptTest.java`. These test check if all of the implemented Javascript features work (operations, variables of different type, if branches, while loops and arrays). It also runs both testing examples.

## Possible future work
Another features which could be (relatively) easily implemented:
 - Lexical context for blocks of code (just using already implemented frameDescriptors stack)
 - another operations (>= etc.)
 - functions (as there is already JSFunction type implemented)
 - better array inicialization with multiple values

## Sources
Implementation was based on [Brainfuck Truffle interpreter](https://github.com/japod/bf) and the insights from [Writing a Language in Truffle. Part 2: Using Truffle and Graal tutorial](http://cesquivias.github.io/blog/2014/12/02/writing-a-language-in-truffle-part-2-using-truffle-and-graal/) were used. Application uses Mozzila Nashorn as a JavaScript parser.

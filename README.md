# JSWithTruffle
JavaScript runtime implementation (partial) in Java using Truffle.

## Language description
Application implements subset of [Ecmascript](http://www.ecma-international.org/publications/standards/Ecma-262.htm), specifically:
 - variables
 - if branches
 - while loops
 - arrays accessible via [] operation
 - null, undefined
 - console output via console.log()
 
## Application example
Aplication requires GRAAL VM.

 1. Build the app using `mvn package`
 2. Run the app  `./js FILENAME_TODO.js`
 3. Examine the output in the console

This application is TODO 
 
The application should output TODO
 
### How to run your own code
Insert your javascript file in  `/src/main/resources/test/`, build the app and run it with the correct filename.

## Unit tests
Basic unit tests can be found in TODO.

## Sources
Implementation was based on [Brainfuck Truffle interpreter](https://github.com/japod/bf) and the insights from [Writing a Language in Truffle. Part 2: Using Truffle and Graal tutorial](http://cesquivias.github.io/blog/2014/12/02/writing-a-language-in-truffle-part-2-using-truffle-and-graal/) were used.

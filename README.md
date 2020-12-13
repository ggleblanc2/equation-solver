# Equation Solver

## Introduction

The JavaScript language has an eval function.  The eval function evaluates JavaScript code represented as a string.  

One of the uses of the JavaScript eval function is to solve mathematical equations.  As long as the mathematical equation is valid JavaScript, the eval function will compute the answer.

Java doesn’t have the equivalent of the eval function.  I decided it would be fun to write a Java application that would solve a mathematical equation input as a String.

What do I mean by a mathematical equation?  Here’s one example.

    1+2*3(30+4/2-(10+2))*-2+1

From this example, we can deduce some rules.

1. All operations within the innermost parenthesis must be completed before operations outside of the parenthesis.

2. The order of operations is multiplication, division, addition, and subtraction.

3. A number followed by an open parenthesis implies multiplication

4. A minus sign starting the equation or following one of the mathematical operators is a unary minus.  A unary minus makes the following number negative.

The Java application that I wrote does not include any mathematical operations besides multiplication, division, addition, or subtraction.  Including trigonometric functions or other unary mathematical functions would increase the complexity of my Java application.

I also did not include any error checking when parsing the mathematical equation.  I assume that the equation is correctly formatted according to the rules I defined.

## Test Results

Here are the results of one of my test runs.  You can see how my equation solver code solves the equation.

    Original Equation:            1+2*3(30+4/2-(10+2))*-2+1
    Expanded Equation:            1 + 2 * 3 ( 30 + 4 / 2 - ( 10 + 2 ) ) * - 2 + 1
    Addition:                     1 + 2 * 3 ( 30 + 4 / 2 - ( 12.0 ) ) * - 2 + 1
    Parenthesis Removed:          1 + 2 * 3 ( 30 + 4 / 2 - 12.0 ) * - 2 + 1
    Division:                     1 + 2 * 3 ( 30 + 2.0 - 12.0 ) * - 2 + 1
    Addition:                     1 + 2 * 3 ( 32.0 - 12.0 ) * - 2 + 1
    Subtraction:                  1 + 2 * 3 ( 20.0 ) * - 2 + 1
    Parenthesis Removed:          1 + 2 * 3 20.0 * - 2 + 1
    Add Multiplication Operator:  1 + 2 * 3 * 20.0 * - 2 + 1
    Unary Minus:                  1 + 2 * 3 * 20.0 * -2.0 + 1
    Multiplication:               1 + 6.0 * 20.0 * -2.0 + 1
    Multiplication:               1 + 120.0 * -2.0 + 1
    Multiplication:               1 + -240.0 + 1
    Addition:                     -239.0 + 1
    Addition:                     -238.0
    
## Explanation

My main method sets up and calculates the result for several mathematical equations represented as Strings.

My Java application is divided into two main sections.  One section parses the mathematical equation String, while the other section processes (computes) the result of the equation.

Here is the call hierarchy of the methods in my Java application.

    main
        equationSolver
            parseEquation
                addPart
                contains
            processEquation
                findFirstCloseParenthesis
                findMatchingOpenParenthesis
                addMissingOperator
                    valueOf
                processUnaryMinus
                    valueOf
                    makeValueNegative
                        valueOf
                processEquation
                    computeValue
                    toString
                removeParenthesis
                toString

Yes, I have two processEquation methods.  The first one (the primary one) processes the entire mathematical equation.  The second one process the equation after the parenthesis have been determined, after any implied multiplication operators have been added, and after any unary minus signs have been processed.

This is called method overloading.  Two methods can have the same name if their input parameters have different signatures.

The parseEquation method is fairly simple.  I loop through the characters of the mathematical equation String, dividing the tokens into numbers and mathematical operators.

I chose not to use a regular expression to parse the equation String.  One, I don’t like excessively long regular expressions.  Two, by writing my own parser, I was able to process the characters of the equation String one time. 

The processEquation method is much more complicated, which is why I created the call hierarchy of the methods.

The first thing I do is find the innermost matching parenthesis.  Next, within the parenthesis, I look for missing multiplication operators and unary minus signs.  Finally, I process the equation, doing multiplication, division, addition, and finally, subtraction.

I repeat this process until I have one number, which is the answer to the equation.

When removing individual parts from the List of parts, one has to be careful.  The index into the List as well as the end position change.

The rest of the methods are straightforward and don’t require any additional explanation.

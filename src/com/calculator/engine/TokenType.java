package com.calculator.engine;

public enum TokenType {
    NUMBER,     // e.g., 42, 3.14
    OPERATOR,   // +, -, *, /, %, ^
    LPAREN,     // (
    RPAREN,     // )
    FUNCTION,   // sin, cos, sqrt, etc.
    VARIABLE    // x, ans
}
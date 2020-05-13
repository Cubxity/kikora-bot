package dev.cubxity.kikora.utils

import kotlin.math.abs
import kotlin.math.floor

fun Double.asFraction(): Pair<Int, Int> {
    val tolerance = 1.0E-6
    var h1 = 1.0
    var h2 = 0.0
    var k1 = 0.0
    var k2 = 1.0
    var b: Double = this
    do {
        val a = floor(b)
        var aux = h1
        h1 = a * h1 + h2
        h2 = aux
        aux = k1
        k1 = a * k1 + k2
        k2 = aux
        b = 1 / (b - a)
    } while (abs(this - h1 / k1) > this * tolerance)

    return h1.toInt() to k1.toInt()
}

fun Pair<Int, Int>.asLatex() = "\\frac{$first}{$second}"
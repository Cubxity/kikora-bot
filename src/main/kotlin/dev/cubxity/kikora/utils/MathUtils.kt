/*
 *     Kikora-bot: Fast and powerful solving bot for Kikora
 *     Copyright (C) 2020 Cubxity
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
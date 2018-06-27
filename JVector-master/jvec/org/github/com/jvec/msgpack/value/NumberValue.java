/*
 * MIT License
 *
 * Copyright (c) 2017 Distributed clocks
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

//
// MessagePack for Java
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//
package org.github.com.jvec.msgpack.value;

import java.math.BigInteger;

/**
 * Base interface of {@link IntegerValue} and {@link FloatValue} interfaces. To extract primitive type values, call toXXX methods, which may lose some information by rounding or truncation.
 *
 * @see IntegerValue
 * @see FloatValue
 */
public interface NumberValue
        extends Value
{
    /**
     * Represent this value as a byte value, which may involve rounding or truncation of the original value.
     * the value.
     */
    byte toByte();

    /**
     * Represent this value as a short value, which may involve rounding or truncation of the original value.
     */
    short toShort();

    /**
     * Represent this value as an int value, which may involve rounding or truncation of the original value.
     * value.
     */
    int toInt();

    /**
     * Represent this value as a long value, which may involve rounding or truncation of the original value.
     */
    long toLong();

    /**
     * Represent this value as a BigInteger, which may involve rounding or truncation of the original value.
     */
    BigInteger toBigInteger();

    /**
     * Represent this value as a 32-bit float value, which may involve rounding or truncation of the original value.
     */
    float toFloat();

    /**
     * Represent this value as a 64-bit double value, which may involve rounding or truncation of the original value.
     */
    double toDouble();
}
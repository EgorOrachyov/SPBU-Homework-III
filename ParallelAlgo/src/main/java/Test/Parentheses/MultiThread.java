/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package Test.Parentheses;

import Application.ParenthesesBalance.CheckMultithread;
import Application.ParenthesesBalance.Load;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 2, time = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class MultiThread {

    @State(Scope.Benchmark)
    private static class TestCase {

        @Param({"2","4","8","16"})
        String threadsCount;

        @Param({"1","2","3"})
        String inputType;

        CheckMultithread check;
        String input;

        @Setup(Level.Trial)
        public void prepare() {
            check = new CheckMultithread(Integer.valueOf(threadsCount));

            if (inputType.equals("1")) {
                input = Load.fromFile("Test/Parentheses/Input/string1");
            }
            else if (inputType.equals("2")) {
                input = Load.fromFile("Test/Parentheses/Input/string2");
            }
            else if (inputType.equals("3")) {
                input = Load.fromFile("Test/Parentheses/Input/string3");
            }
        }
    }

    @Benchmark
    public void test(Blackhole bh, TestCase tc) {
        bh.consume(tc.check.compute(tc.input));
    }
}

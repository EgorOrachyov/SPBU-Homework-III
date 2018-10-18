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

package Test.LinearReduction;

import Application.LinearReduction.Data;
import Application.LinearReduction.Load;
import Application.LinearReduction.SolverMultithread;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 2, time = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class MultiThread {

    private static Data input1;
    private static Data input2;
    private static Data input3;

    private static SolverMultithread solver2;
    private static SolverMultithread solver4;
    private static SolverMultithread solver8;
    private static SolverMultithread solver16;

    static {

        input1 = Load.fromFile("Test/LinearReduction/Input/linear1");
        input2 = Load.fromFile("Test/LinearReduction/Input/linear2");
        input3 = Load.fromFile("Test/LinearReduction/Input/linear3");

        solver2 = new SolverMultithread(2);
        solver4 = new SolverMultithread(4);
        solver8 = new SolverMultithread(8);
        solver16 = new SolverMultithread(16);

    }

    @Benchmark
    public void thread2(Blackhole bh) {
        bh.consume(solver2.compute(input1.a, input1.b));
        bh.consume(solver2.compute(input2.a, input2.b));
        bh.consume(solver2.compute(input3.a, input3.b));
    }

    @Benchmark
    public void thread4(Blackhole bh) {
        bh.consume(solver4.compute(input1.a, input1.b));
        bh.consume(solver4.compute(input2.a, input2.b));
        bh.consume(solver4.compute(input3.a, input3.b));
    }

    @Benchmark
    public void thread8(Blackhole bh) {
        bh.consume(solver8.compute(input1.a, input1.b));
        bh.consume(solver8.compute(input2.a, input2.b));
        bh.consume(solver8.compute(input3.a, input3.b));
    }

    @Benchmark
    public void thread16(Blackhole bh) {
        bh.consume(solver16.compute(input1.a, input1.b));
        bh.consume(solver16.compute(input2.a, input2.b));
        bh.consume(solver16.compute(input3.a, input3.b));
    }

}

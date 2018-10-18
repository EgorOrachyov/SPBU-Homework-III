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

package Test.TurtleMovement;


import Application.TurtleMovement.Load;
import Application.TurtleMovement.Transform;
import Application.TurtleMovement.TransformMultithread;
import Application.TurtleMovement.Vector2d;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 2, time = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class MultiThread {

    private static Vector2d[] input1;
    private static Vector2d[] input2;
    private static Vector2d[] input3;

    private static TransformMultithread transform2;
    private static TransformMultithread transform4;
    private static TransformMultithread transform8;
    private static TransformMultithread transform16;

    static {

        input1 = Load.fromFile("Test/TurtleMovement/Input/way1");
        input2 = Load.fromFile("Test/TurtleMovement/Input/way2");
        input3 = Load.fromFile("Test/TurtleMovement/Input/way3");

        transform2 = new TransformMultithread(2);
        transform4 = new TransformMultithread(4);
        transform8 = new TransformMultithread(8);
        transform16 = new TransformMultithread(16);
        
    }

    @Benchmark
    public void thread2(Blackhole bh) {
        bh.consume(transform2.move(input1));
        bh.consume(transform2.move(input2));
        bh.consume(transform2.move(input3));
    }

    @Benchmark
    public void thread4(Blackhole bh) {
        bh.consume(transform4.move(input1));
        bh.consume(transform4.move(input2));
        bh.consume(transform4.move(input3));
    }

    @Benchmark
    public void thread8(Blackhole bh) {
        bh.consume(transform8.move(input1));
        bh.consume(transform8.move(input2));
        bh.consume(transform8.move(input3));
    }

    @Benchmark
    public void thread16(Blackhole bh) {
        bh.consume(transform16.move(input1));
        bh.consume(transform16.move(input2));
        bh.consume(transform16.move(input3));
    }

}

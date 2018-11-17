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

package Test;

import Application.Concurrent.*;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Fork(1)
@Warmup(iterations = 2, time = 8)
@Measurement(iterations = 3, time = 8)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class TTAS {

    private static Lock lock = new TTASLock();
    private static Data data = new Data();
    private static int[] size = {1000000};
    private static LockWorker[] worker;

    static {
        worker = new LockWorker[size.length];
        for(int i = 0; i < size.length; i++) {
            worker[i] = new LockWorker(data, size[i], lock);
        }
    }

    ////////////////////////////////////////////
    ///                                       //
    ///           Threads Count 2             //
    ///                                       //
    ////////////////////////////////////////////

    //@TASvsTTAS
    public void threads_2(Blackhole bh) {
        worker[0].run(2);
        bh.consume(worker[0]);
    }

    ////////////////////////////////////////////
    ///                                       //
    ///           Threads Count 4             //
    ///                                       //
    ////////////////////////////////////////////

    //@TASvsTTAS
    public void threads_4(Blackhole bh) {
        worker[0].run(4);
        bh.consume(worker[0]);
    }

    ////////////////////////////////////////////
    ///                                       //
    ///           Threads Count 8             //
    ///                                       //
    ////////////////////////////////////////////

    //@TASvsTTAS
    public void threads_8(Blackhole bh) {
        worker[0].run(8);
        bh.consume(worker[0]);
    }

    ////////////////////////////////////////////
    ///                                       //
    ///           Threads Count 16            //
    ///                                       //
    ////////////////////////////////////////////

    //@TASvsTTAS
    public void threads_16(Blackhole bh) {
        worker[0].run(16);
        bh.consume(worker[0]);
    }

}

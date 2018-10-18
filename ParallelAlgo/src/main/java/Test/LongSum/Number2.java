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

package Test.LongSum;

import Application.LongAdd.Add;
import Application.LongAdd.AddMultithread;
import Application.LongAdd.DecimalValue;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 2, time = 5)
@Measurement(iterations = 2, time = 5)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class Number2 {

    private static DecimalValue a;
    private static DecimalValue b;

    private static Add thread1;

    private static AddMultithread thread2;
    private static AddMultithread thread4;
    private static AddMultithread thread8;
    private static AddMultithread thread16;

    static {
        a = new DecimalValue(
                "231764962798236473879827982574" +
                "543636536339875947882843869473" +
                "486325462354682375974598743890" +
                "327846873422549080598429080928" +
                "919479187419847983789332667326" +
                "723979111414422387686879049723" +
                "098200807852911946912647868474" +
                "241289871289398729837899823987" +
                "498179849317892700480534034752" +
                "881375621743761548639769425426" +
                "597268781364781637848999238283" +
                "723687463287648723746835765804");
        b = new DecimalValue(
                "124765672234798789608509860931" +
                "143059038096587983795278682876" +
                "723678278587423984729875982789" +
                "809239849823148164786174678114" +
                "719874893723984897489785730860" +
                "738274924657387034809849053546" +
                "752194801761485758690830276468" +
                "233252352523523423425667809079" +
                "363565635635667876675764542008" +
                "453435346547809023498275682368" +
                "432518939290898398234236345754" +
                "847695809868745394858348946297");

        thread1 = new Add();

        thread2 = new AddMultithread(2);
        thread4 = new AddMultithread(4);
        thread8 = new AddMultithread(8);
        thread16 = new AddMultithread(16);
    }

    ///////////////////////////////////
    ///                             ///
    ///   Single thread algorithm   ///
    ///                             ///
    ///////////////////////////////////

    @Benchmark
    public void thread1(Blackhole bh) {
        bh.consume(thread1.apply(a, b));
    }

    ///////////////////////////////////
    ///                             ///
    ///   Multi-thread algorithm    ///
    ///                             ///
    ///////////////////////////////////

    @Benchmark
    public void thread2(Blackhole bh) {
        bh.consume(thread2.apply(a, b));
    }

    @Benchmark
    public void thread4(Blackhole bh) {
        bh.consume(thread4.apply(a, b));
    }

    @Benchmark
    public void thread8(Blackhole bh) {
        bh.consume(thread8.apply(a, b));
    }

    @Benchmark
    public void thread16(Blackhole bh) {
        bh.consume(thread16.apply(a, b));

    }

}

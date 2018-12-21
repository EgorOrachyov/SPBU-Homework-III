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

import Client.AsyncClient;
import Client.FilterTask;
import Filter.Image;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 2, time = 30)
@Measurement(iterations = 3, time = 30)
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.SingleShotTime)
public class ServiceTimeWithoutLoad {

    @State(Scope.Benchmark)
    public static class TestCase {

        @Param({"1","3","6"})
        String taskCount;

        @Param({"/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/test2.jpg",
                "/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/test4.jpg"})
        String imageName;

        int cycles;
        Image image;
        AsyncClient client;

        final int FILTER_ID = 1;

        @Setup(Level.Trial)
        public void prepare() {
            try {
                client = new AsyncClient("localhost", 40000);
                cycles = Integer.valueOf(taskCount);
                image = new Image(imageName);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @TearDown(Level.Trial)
        public void shutdown() {
            if (client != null) {
                client.done(true);
            }
        }

    }

    @Benchmark
    public void test(TestCase tc, Blackhole bh) {
        for (int i = 0; i < tc.cycles; i++) {
            tc.client.submitTask(new FilterTask(tc.image, tc.FILTER_ID));

            FilterTask result;

            do {
                result = tc.client.getCompletedTasks().poll();
            } while (result == null);
        }

        bh.consume(tc.client);
    }

}

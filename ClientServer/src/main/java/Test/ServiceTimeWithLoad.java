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
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 1, time = 30)
@Measurement(iterations = 1, time = 30)
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.SingleShotTime)
public class ServiceTimeWithLoad {

    @State(Scope.Benchmark)
    public static class TestCase {

        @Param({"1"})
        String p_taskCount;

        @Param({"20","40"})
        String p_clientsCount;

        @Param({"/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/test2.jpg"})
        String imageName;

        String[] imageNames = {
                "/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/test2.jpg",
                "/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/test4.jpg"
        };

        int clientsCount;
        int cycles;
        Image image;
        ArrayList<Image> images;
        ArrayList<AsyncClient> clients;

        @Setup(Level.Trial)
        public void prepare() {
            try {
                clientsCount = Integer.valueOf(p_clientsCount);
                cycles = Integer.valueOf(p_taskCount);
                image = new Image(imageName);

                images = new ArrayList<>(imageNames.length);
                clients = new ArrayList<>(clientsCount);

                for (String name : imageNames) {
                    images.add(new Image(name));
                }

                for (int i = 0; i < clientsCount; i++) {
                    clients.add(new AsyncClient("localhost", 40000));
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @TearDown(Level.Trial)
        public void shutdown() {
            for (AsyncClient client : clients) {
                client.done(true);
            }
        }

    }

    @Benchmark
    public void fixedPixelsCount(TestCase tc, Blackhole bh) {

        for (int i = 0; i < tc.cycles; i++) {

            for (AsyncClient client : tc.clients) {
                client.submitTask(new FilterTask(tc.image, 1));
            }

            for (AsyncClient client : tc.clients) {
                FilterTask task;
                do {
                    task = client.getCompletedTasks().poll();
                } while (task == null);
            }

        }

        bh.consume(tc.clients);

    }

    @Benchmark
    public void varPixelsCount(TestCase tc, Blackhole bh) {

        for (int i = 0; i < tc.cycles; i++) {

            for (AsyncClient client : tc.clients) {
                client.submitTask(new FilterTask(tc.images.get(0), 1));
            }

            for (AsyncClient client : tc.clients) {
                FilterTask task;
                do {
                    task = client.getCompletedTasks().poll();
                } while (task == null);
            }

            for (AsyncClient client : tc.clients) {
                client.submitTask(new FilterTask(tc.images.get(1), 1));
            }

            for (AsyncClient client : tc.clients) {
                FilterTask task;
                do {
                    task = client.getCompletedTasks().poll();
                } while (task == null);
            }

        }

        bh.consume(tc.clients);

    }

}

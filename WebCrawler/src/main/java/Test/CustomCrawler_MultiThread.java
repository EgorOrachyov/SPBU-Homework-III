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

import Application.Concurrent.LinkedList;
import Application.Web.Custom.Crawler;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 1, time = 20)
@Measurement(iterations = 0, time = 10)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class CustomCrawler_MultiThread {

    private static Crawler crawler[];
    private static final String url = "http://www.shaderx.com";
    private static final String path = "/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/WebCrawler/src/main/Test";
    private static final int[] threads = {2,4,8,16};

    static {
        crawler = new Crawler[threads.length];
        for(int i = 0; i < threads.length; i++) {
            crawler[i] = new Crawler(threads[i]);
        }
    }

    // @Benchmark
    public void threads_2(Blackhole bh) {
        //LinkedList<String> result = crawler[0].download(url,1, 1);
        crawler[0].download(url,1, path,1);
        //bh.consume(result);
    }

    // @Benchmark
    public void threads_4(Blackhole bh) {
        //LinkedList<String> result = crawler[1].download(url,1, 1);
        crawler[1].download(url, 1, path, 1);
        //bh.consume(result);
    }

    // @Benchmark
    public void threads_8(Blackhole bh) {
        LinkedList<String> result = crawler[2].download(url,1, 1);
        bh.consume(result);
    }

    // @Benchmark
    public void threads_16(Blackhole bh) {
        LinkedList<String> result = crawler[3].download(url,1, 1);
        bh.consume(result);
    }

}

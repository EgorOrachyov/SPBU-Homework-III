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

import Filter.Blur.AverageBlur;
import Filter.Common.Image;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 4)
@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
public class AverageBlurTestPicture3 {
    private static TestData data;

    static {
        // Test data for desired picture
        data = new TestData("pictures/test3.jpg", AverageBlur.RANGE_TYPE_7);
    }

    //////////////////////////////////////////////////////////
    ///                                                    ///
    ///           Horizontal Pass (cash friendly)          ///
    ///                                                    ///
    //////////////////////////////////////////////////////////

    @Benchmark
    public void BlurHR_Threads1() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(0));
        filter.setPassType(AverageBlur.PASS_TYPE_HORIZONTAL);
        Image result = filter.apply(data.picture);
    }

    @Benchmark
    public void BlurHR_Threads2() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(1));
        filter.setPassType(AverageBlur.PASS_TYPE_HORIZONTAL);
        Image result = filter.apply(data.picture);
    }

    @Benchmark
    public void BlurHR_Threads4() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(2));
        filter.setPassType(AverageBlur.PASS_TYPE_HORIZONTAL);
        Image result = filter.apply(data.picture);
    }

    @Benchmark
    public void BlurHR_Threads8() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(3));
        filter.setPassType(AverageBlur.PASS_TYPE_HORIZONTAL);
        Image result = filter.apply(data.picture);
    }

    @Benchmark
    public void BlurHR_Threads16() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(4));
        filter.setPassType(AverageBlur.PASS_TYPE_HORIZONTAL);
        Image result = filter.apply(data.picture);
    }

    //////////////////////////////////////////////////////////
    ///                                                    ///
    ///        Vertical Pass (not cash friendly ?)         ///
    ///                                                    ///
    //////////////////////////////////////////////////////////

    @Benchmark
    public void BlurVR_Threads1() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(0));
        filter.setPassType(AverageBlur.PASS_TYPE_VERTICAL);
        Image result = filter.apply(data.picture);
    }

    @Benchmark
    public void BlurVR_Threads2() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(1));
        filter.setPassType(AverageBlur.PASS_TYPE_VERTICAL);
        Image result = filter.apply(data.picture);
    }

    @Benchmark
    public void BlurVR_Threads4() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(2));
        filter.setPassType(AverageBlur.PASS_TYPE_VERTICAL);
        Image result = filter.apply(data.picture);
    }

    @Benchmark
    public void BlurVR_Threads8() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(3));
        filter.setPassType(AverageBlur.PASS_TYPE_VERTICAL);
        Image result = filter.apply(data.picture);
    }

    @Benchmark
    public void BlurVR_Threads16() {
        AverageBlur filter = new AverageBlur(data.rangeType);
        filter.setThreadsCount(data.threads.get(4));
        filter.setPassType(AverageBlur.PASS_TYPE_VERTICAL);
        Image result = filter.apply(data.picture);
    }
}

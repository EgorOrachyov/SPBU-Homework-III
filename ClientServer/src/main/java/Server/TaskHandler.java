package Server;

import Filter.FilterBehavior;
import Filter.Image;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskHandler implements Runnable {

    private AtomicBoolean processDone;
    private AtomicInteger progress;
    private Image source;
    private Image result;
    private FilterBehavior behavior;

    public TaskHandler(Image source, FilterBehavior behavior) {
        this.processDone = new AtomicBoolean(false);
        this.progress = new AtomicInteger(0);

        this.source = source;
        this.behavior = behavior;
    }

    public Image getResult() {
        return result;
    }

    public AtomicBoolean getProcessDone() {
        return processDone;
    }

    public AtomicInteger getProgress() {
        return progress;
    }

    @Override
    public void run() {

        if (!processDone.get()) {

            final int width = source.getWidth();
            final int height = source.getHeight();

            result = new Image(width, height);
            behavior.prepareProcess(source);

            int data[] = result.serialize();

            for (int y = 0; y < height; ++y) {

                //System.out.println("Progress at task: " + ((int)(100.0f * ((float) y / (float) height))));
                progress.set((int)(100.0f * ((float) y / (float) height)));

                for (int x = 0; x < width; ++x) {
                    data[y * width + x] = behavior.processPixel(x, y);
                }

            }

            behavior.finishProcess();
            result.setBytes(data);

            processDone.set(true);
        }

    }

}
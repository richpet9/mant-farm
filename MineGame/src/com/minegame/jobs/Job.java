package com.minegame.jobs;

import com.minegame.world.Cell;
import com.minegame.world.Mant;

/**
 * Jobs are assigned to Mants, and job queues are stores in Handler
 */
public abstract class Job {
    protected JobID id;
    protected Cell targetCell;
    protected int duration;     //In Seconds
    protected Mant worker;
    protected boolean dequeue;  //Flag for dequeueing
    protected boolean complete; //Flag for removing from memory
    protected long startTime = -1;    //for calculating durations

    abstract void onAssigned();
    abstract void onComplete(boolean success);

    public void work() {
        if(!worker.isWorking()) worker.setWorking(true);
        if(startTime == -1) startTime = System.nanoTime();

        long elapsedTime = (System.nanoTime() - startTime) / (long) 1E9;
        if(elapsedTime >= duration) {
            onComplete(true);
            complete = true;
        }
    }

    //Getters
    public Cell getTargetCell() {
        return targetCell;
    }
    public int getDuration() {
        return duration;
    }
    public Mant getWorker() {
        return worker;
    }
    public boolean isDequeue() {
        return dequeue;
    }
    public boolean isComplete() {
        return complete;
    }

    //Setters
    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setWorker(Mant worker) {
        this.worker = worker;
        onAssigned();
    }
    public void setDequeue(boolean dequeue) {
        this.dequeue = dequeue;
    }
    public void setComplete(boolean complete) {
        this.complete = complete;
        onComplete(false);
    }
}

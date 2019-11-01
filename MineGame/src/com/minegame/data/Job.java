package com.minegame.data;

import com.minegame.world.Cell;
import com.minegame.world.Mant;

/**
 * Jobs are assigned to Mants, and job queues are stores in Handler
 */
public abstract class Job {
    protected JobID id;
    protected Cell targetCell;
    protected int duration;     //In Seconds
    protected long startTime = -1;    //for calculating durations
    protected Mant worker;
    protected boolean dequeue;  //Flag for dequeueing
    protected boolean complete; //Flag for removing from memory

    abstract void onAssigned();
    abstract void onComplete(boolean success);
    public abstract void work();

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

package com.minegame.jobs;

import com.minegame.world.Bomb;
import com.minegame.world.Cell;

public class BombJob extends Job {
    private Bomb bomb;
    private JobType type;

    public enum JobType { ARM, DISARM };

    public BombJob(Cell targetCell, Bomb bomb, JobType type) {
        this.id = JobID.BOMB;
        this.targetCell = targetCell;
        this.bomb = bomb;
        this.type = type;
        this.duration = 1;
    }

    @Override
    void onAssigned() {
        bomb.setHasJob(true);
    }

    @Override
    protected void onComplete(boolean success) {
        if(success) {
            bomb.setArmed(true);
        } else {
            bomb.setHasJob(false);
        }

        worker.setJob(null);
        worker.setWorking(false);
    }

    @Override
    public void work() {
        super.work();
    }
}

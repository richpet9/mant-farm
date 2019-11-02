package com.minegame.jobs;

import com.minegame.world.Bomb;
import com.minegame.world.Cell;

public class BombJob extends Job {
    private Bomb bomb;
    private JobType type;

    public enum JobType { ARM, DEFUSE };

    public BombJob(Cell targetCell, Bomb bomb, JobType type) {
        this.id = JobID.BOMB;
        this.targetCell = targetCell;
        this.bomb = bomb;
        this.type = type;
        this.duration = 1;
    }

    @Override
    void onAssigned() {

    }

    @Override
    protected void onComplete(boolean success) {
        bomb.setArmed(true);

        worker.setJob(null);
        worker.setWorking(false);
    }

    @Override
    public void work() {
        super.work();
    }
}

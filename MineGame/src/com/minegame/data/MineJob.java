package com.minegame.data;

import com.minegame.world.Cell;
import com.minegame.world.Element;

public class MineJob extends Job {

    public MineJob(Cell targetCell) {
        this.id = JobID.Mine;
        this.targetCell = targetCell;
        this.duration = 3;

        //This is a little safety check, just in case air gets selected for mining somehow
        if(targetCell.isAir()) complete = true;
    }

    @Override
    void onAssigned() {

    }

    @Override
    void onComplete(boolean success) {
        if(success) {
            targetCell.setDropChunk(targetCell.getElement());
            targetCell.setElement(Element.AIR);
        }

        targetCell.setOverlay(false);
        worker.setJob(null);
        worker.setWorking(false);
    }

    @Override
    public void work() {
        if(!worker.isWorking()) worker.setWorking(true);
        if(startTime == -1) startTime = System.nanoTime();

        long elapsedTime = (System.nanoTime() - startTime) / (long) 1E9;
        if(elapsedTime >= duration) {
            onComplete(true);
            complete = true;
        }
    }
}

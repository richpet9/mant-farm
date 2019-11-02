package com.minegame.jobs;

import com.minegame.world.Cell;
import com.minegame.world.Element;

public class MineJob extends Job {

    public MineJob(Cell targetCell) {
        this.id = JobID.MINE;
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
        super.work();
    }
}

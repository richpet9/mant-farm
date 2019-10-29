package com.minegame.tests;

import com.minegame.data.MineQueue;
import com.minegame.world.Cell;

public class MineQueueTest {
    public static void main(String[] args) {
        MineQueue q = new MineQueue(4);

        //Empty Queue
        System.out.println("\nEmpty Queue:");
        q.display();

        //Add one item to the queue
        System.out.println("\nOne item Queue:");
        q.enqueue(new Cell(0, 0, 10, 0));
        q.display();

        //Add three more items
        System.out.println("\nAdded three more items to item Queue:");
        for(int i = 0; i < 3; i++) {
            q.enqueue(new Cell(0, 0, 11 + i, 0));
        }
        q.display();

        //Dequeue 10
        System.out.println("\nDequeueing 10: " + q.dequeue().getCellX());
        q.display();
        //Dequeue 11
        System.out.println("\nDequeueing 11: " + q.dequeue().getCellX());
        q.display();
        //Dequeue 12
        System.out.println("\nDequeueing 12: " + q.dequeue().getCellX());
        q.display();
        //Dequeue 13
        System.out.println("\nDequeueing 13: " + q.dequeue().getCellX());
        q.display();

        //Add four more items
        System.out.println("\nAdded four more items to item Queue:");
        for(int i = 0; i < 4; i++) {
            q.enqueue(new Cell(0, 0, 10 + i, 0));
        }
        q.display();

        //Add fifth item
        System.out.println("\nadding fifth item to Queue:");
        q.enqueue(new Cell(0, 0, 14, 0));
        q.display();

        q.dequeue();    //10
        q.dequeue();    //11
        q.dequeue();    //12

        for(int i = 0; i < 4; i++) {
            q.enqueue(new Cell(0, 0, i, 0));
        }
        System.out.println("Expected 13: " + q.dequeue().getCellX());
        System.out.println("Expected 14: " + q.dequeue().getCellX());
        System.out.println("Expected 0: " + q.dequeue().getCellX());
        q.display();
    }
}

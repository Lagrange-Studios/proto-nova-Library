package debug;

import java.util.ArrayList;

import collision.EntityCollision;
import protonova.protobuf.EntityProto.Entity;
import protonova.protobuf.VectorProto.Vector;

public class Main {

	public static void main(String[] args) {
		
		Vector size = Vector.newBuilder()
				.setX(1)
				.setY(1)
				.build();
		
		Vector pos1 = Vector.newBuilder()
    			.setX(-38.0f)
    			.setY(15)
    			.build();
    	
    	Entity entity1 = Entity.newBuilder()
    			.setPosition(pos1)
    			.setSize(size)
    			.build();
    	
    	Vector pos2 = Vector.newBuilder()
    			.setX(-38.911484f)
    			.setY(15.213504f)
    			.build();
    	
    	Entity entity2 = Entity.newBuilder()
    			.setPosition(pos2)
    			.setSize(size)
    			.build();
    	
    	long startTime = System.currentTimeMillis();
    	
    	try {
        	System.out.println(EntityCollision.checkCollision(entity1, entity2));
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	System.out.println("Operation took: "+(System.currentTimeMillis()-startTime)+" miliseconds");
		/*
		int amount = 10000000;
    	int range = 1000;
		
    	ArrayList<Entity> entities1 = new ArrayList<Entity>();
    	ArrayList<Entity> entities2 = new ArrayList<Entity>();
    	for(int i=0;i<amount;i++) {
			Vector pos1 = Vector.newBuilder()
	    			.setX((float) ((Math.random()-.5)*range))
	    			.setY((float) ((Math.random()-.5)*range))
	    			.build();
	    	
	    	Entity entity1 = Entity.newBuilder()
	    			.setPosition(pos1)
	    			.build();
	    	
	    	Vector pos2 = Vector.newBuilder()
	    			.setX((float) ((Math.random()-.5)*range))
	    			.setY((float) ((Math.random()-.5)*range))
	    			.build();
	    	
	    	Entity entity2 = Entity.newBuilder()
	    			.setPosition(pos2)
	    			.build();
	    	entities1.add(entity1);
	    	entities2.add(entity2);
    	}

    	long startTime = System.currentTimeMillis();
	    for(int i=0;i<amount;i++) {
	    	System.out.println(EntityCollision.checkCollision(entities1.get(i), entities2.get(i)));
		}

    	System.out.println("Operation took: "+(System.currentTimeMillis()-startTime)+" miliseconds for "+amount+" entities");
    	System.out.println("Average ms per calculation: "+(double) ((double) (System.currentTimeMillis()-startTime)/amount));*/
	}

}

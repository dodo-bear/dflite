import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
/*ViewPriority Layers:
 * 0. UI elements
 * 1. Dwarves
 * 2. Animal subtypes
 * 3. Items
 * 4. Large objects
 * 5. Structures
 * 6. Terrain
 * 7. Background
 * 
 * 
 * 
*/
public class GameManager {
    static long last_time = System.nanoTime();
    static long elapsedTime;
    static boolean isRunning = true;
    static MyFrame window;
    static boolean paused = false;
    static ControlHandler controls;
    public static WorldGrid world;
    public static void main(String[] args) {
        try{
        world = new WorldGrid("world", 32, 32);
        }catch (Exception e){
            System.out.println("World not created: " + e.getMessage());
        }
        System.out.println("World Created");
        try{
        window = new MyFrame("DwarfFortressLite", world);
        window.setVisible(true);
        controls = window.getControls();
        }catch(Exception e){
            System.out.println("Window not created: " + e.getMessage());
        }
        System.out.println("Window Open");
        run();
    }
    private static Point checkCoords(int x, int y){ //Caps coordinates to within world bounds
        int xCoord = 0;
        int yCoord = 0;
        if(x > world.getXSize()-32){xCoord = world.getXSize()-32;}else if(x < 0){xCoord = 0;}else{xCoord = x;}
        if(y > world.getYSize()-32){yCoord = world.getYSize()-32;}else if(y < 0){yCoord = 0;}else{yCoord = y;}
        return new Point(xCoord, yCoord);
    }

    public static void run() {
		long lastime = System.nanoTime();
		double controlUpdatesPerSecond = 20; //Control updates per second
		int ticksPerSecond = 5; //Game updates per second
		double ns = 1000000000 / controlUpdatesPerSecond;
		double delta = 0;
		int frames = 0;
        int ticks = 0;
        int countDownToTick = 0;
		double time = System.currentTimeMillis();
		double x = 0; //Current "camera" coordinates, corresponds to upper leftmost visible square
        double y = 0;
        
        
		while(isRunning == true) {
			long now = System.nanoTime();
			delta += (now - lastime) / ns;
			lastime = now;
            frames++;
            
            //Refresh the display, returns how long it took in nanoseconds
            long elapsedt = window.refreshGraphics(checkCoords((int)x, (int)y).x, checkCoords((int)x, (int)y).y);
            
			if(delta >= 1) {
                if(paused == false){
                	//Update coordinates based on camera control input
                	x += controls.getYMove();
                    y += controls.getXMove();
                    x = checkCoords((int)x, (int)y).x;
                    y = checkCoords((int)x, (int)y).y;
                    ControlUpdate();
                    
                    if(countDownToTick == ticksPerSecond) {
                    	GameUpdate();
                    	countDownToTick = 0;
                    }
                    countDownToTick +=1;
                    
                }
				ticks++;
				delta--;
				//Debug output to console
				if(System.currentTimeMillis() - time >= 1000) {
					System.out.println("fps: " + frames + " ups: " + ticks + ", mspf: " + elapsedt/1000000f + ", Current coordinates: " + checkCoords((int)x, (int)y).x + ", " + checkCoords((int)x, (int)y).y + " PriorityObject: " + world.getSquare(checkCoords((int)x, (int)y).x, checkCoords((int)x, (int)y).y).getObjects()[0]);
					time += 1000;
					ticks = 0;
				}
            frames = 0;
			}
		}
	}
    public static void GameUpdate() { //All object interactions occur here.
        ArrayList<Animal> animals = new ArrayList<Animal>();
        for (GridSquare gridSquare : world.getAllGridSquares()) { //Move everything that can move
            for (Animal animal : Arrays.copyOf(gridSquare.findObjectsWithType("Animal"), gridSquare.findObjectsWithType("Animal").length, Animal[].class)) {
                if(!animals.contains(animal)){
                    animal.Move();
                    animals.add(animal);
                }
            }
        }
        ArrayList<FruitPlant> fruitTrees = new ArrayList<FruitPlant>();
        for (GridSquare gridSquare : world.getAllGridSquares()) { //Grow all plants that can grow
            for (FruitPlant fruitPlant : Arrays.copyOf(gridSquare.findObjectsWithType("Plant"), gridSquare.findObjectsWithType("Plant").length, FruitPlant[].class)) {
                if(!fruitTrees.contains(fruitPlant)){
                fruitPlant.grow();
                fruitTrees.add(fruitPlant);
                }
            }
        }
    }
    
    public static void ControlUpdate() {
    	
    }
    
}
package Primary;

import java.util.LinkedList;

class TestTCS extends Thread {

    private int count = 0;
    private Boolean running = true;

    public void begin(){

        // Added some simple testing behavior for the TC where it
        // just flips from green to red every loop

        SignalColor color1, color2;
        LinkedList<Lanes> northsouth = new LinkedList<>();
        LinkedList<Lanes> eastwest = new LinkedList<>();
        for(Lanes l: Lanes.values())
        {
            if(l.toString().contains("N") || l.toString().contains("S")) northsouth.add(l);
            else eastwest.add(l);
        }
        while(running){

            if (count %  4 ==  0){
                color1 = SignalColor.GREEN;
                color2 = SignalColor.RED;
            } else if (count % 4 == 1){
                color1 = SignalColor.YELLOW;
                color2 = SignalColor.RED;
            } else if (count % 4 == 2){
                color1 = SignalColor.RED;
                color2 = SignalColor.GREEN;
            } else {
                color1 = SignalColor.RED;
                color2 = SignalColor.YELLOW;
            }

            for(Lanes l: northsouth)
            {
                l.setColor(color1);
            }

            for(Lanes l: eastwest)
            {
                l.setColor(color2);
            }

            Lights.valueOf("NORTH").setColor(color2);
            Lights.valueOf("SOUTH").setColor(color2);
            Lights.valueOf("WEST").setColor(color1);
            Lights.valueOf("EAST").setColor(color1);



            count ++;

            /*

            for(Lanes j: Lanes.values()){
                j.setColor(SignalColor.YELLOW);
            }

            for(Lanes k: Lanes.values()){
                k.setColor(SignalColor.RED);
            }

            for(Lights l : Lights.values()){
                l.setPedestrianAt(!l.isPedestrianAt());
            }

            for(Lanes m : Lanes.values()){
                System.out.println("Car on lane " + m + " - " + m.getCarOnLane());
            }

            for(Lanes n : Lanes.values()){
                System.out.println("Emergency on lane " + n + " - " + n.getEmergencyOnLane());
            }

            for(Lights p : Lights.values()){
                System.out.println("Pedestrian at light " + p + " - " + p.isPedestrianAt());
            }

            for(Lanes q : Lanes.values()){
                q.setCarOnLane(!q.getCarOnLane());
            }

            for(Lanes r : Lanes.values()){
                r.setEmergencyOnLane(!r.getEmergencyOnLane());
            }
            */


            for(Lanes n : Lanes.values()){
                System.out.println("Emergency on lane " + n + " - " + n.getEmergencyOnLane());
            }

            System.out.println("Loop Done...");
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("test ended..");
    }

    public void end(){
        running = false;
    }

}

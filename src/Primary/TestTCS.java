package Primary;

public class TestTCS extends Thread {

    public void begin(){
        while(true){
            for(Lanes i: Lanes.values()){
                i.setColor(SignalColor.GREEN);
            }

            for(Lanes j: Lanes.values()){
                j.setColor(SignalColor.YELLOW);
            }

            for(Lanes k: Lanes.values()){
                k.setColor(SignalColor.RED);
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

            System.out.println("Loop Done...");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

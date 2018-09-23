package Primary;

class TestTCS extends Thread {

    private Boolean green = false;
    private Boolean running = true;

    public void begin(){

        // Added some simple testing behavior for the TC where it
        // just flips from green to red every loop

        SignalColor color;
        while(running){

            if (green){
                color = SignalColor.GREEN;
            } else {
                color = SignalColor.RED;
            }

            for(Lanes i: Lanes.values()){
                i.setColor(color);
            }

            green = !green;

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

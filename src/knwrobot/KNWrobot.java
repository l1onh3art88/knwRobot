/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package knwrobot;
import rxtxrobot.*;

/**

 *
 * @author spenc_000
 */
public class KNWrobot {
    final private static int PING_PIN = 8; 
    final private static int BUMP_PIN = 2;
    private static int ticks_to_turn_right = 180;
    private static int ticks_to_turn_left = 180;

    //Motor1 is left motor (positive values)
    // Motor 2 is right motor (negative values)
   
    //163.04 ticks per foot
    
    public static void main(String[] args) {
        // TODO code application logic here
        boolean wall = false; // True = wall on right, False = wall on left
        boolean game = true;
        int nav[] = new int[6];
        RXTXRobot r = new ArduinoUno(); // Create RXTXRobot object 
	r.setPort("COM3"); // Set port to COM3
        int ping;
        int distance_one;
        int distance_two;
        int reading;
	r.connect(); 
        r.attachMotor(RXTXRobot.MOTOR1, 5);
        r.attachMotor(RXTXRobot.MOTOR2, 6);
//        r.runEncodedMotor(RXTXRobot.MOTOR1,500,1600,RXTXRobot.MOTOR2,-500,1600);

        //first part
                reset_encoders(r);

        distance_one = r.getEncodedMotorPosition(RXTXRobot.MOTOR1);
        distance_two = r.getEncodedMotorPosition(RXTXRobot.MOTOR2);
         System.out.println("Encoder 1: " + distance_one + "\n Encoder 2: " + distance_two);
        r.runEncodedMotor(RXTXRobot.MOTOR1,500,150,RXTXRobot.MOTOR2,-500,150);
         distance_one = r.getEncodedMotorPosition(RXTXRobot.MOTOR1);
        distance_two = r.getEncodedMotorPosition(RXTXRobot.MOTOR2);
          System.out.println("Encoder 1: " + distance_one + "\n Encoder 2: " + distance_two);
         
        boolean phase_one=true;
        System.out.println("begin");
        if(phase_one==true){
            while(distance_one <600 && distance_two > -600){
                ping = r.getPing(PING_PIN);
                System.out.println(ping);
                if(ping>=40){
                    r.runEncodedMotor(RXTXRobot.MOTOR1,500,150,RXTXRobot.MOTOR2,-500,150);
                    distance_one = r.getEncodedMotorPosition(RXTXRobot.MOTOR1);
                    distance_two = r.getEncodedMotorPosition(RXTXRobot.MOTOR2);
                    System.out.println("Encoder 1: " + distance_one + "\n Encoder 2: " + distance_two);
                }
                else{
                   nav[0] = ping; 
                   navigate_wall(r,nav,wall); 
                } 

            }
            phase_one = false;
        } 
        reset_encoders(r);
        distance_one = r.getEncodedMotorPosition(RXTXRobot.MOTOR1);
        distance_two = r.getEncodedMotorPosition(RXTXRobot.MOTOR2);
//        
        boolean phase_two = true;
        if(phase_one ==false){
            while(distance_one <=900 && distance_two <=900){
                ping = r.getPing(PING_PIN);
                System.out.println(ping);
                if(ping>=50){
                    r.runEncodedMotor(RXTXRobot.MOTOR1,500,150,RXTXRobot.MOTOR2,-500,150);
                    distance_one = r.getEncodedMotorPosition(RXTXRobot.MOTOR1);
                    distance_two = r.getEncodedMotorPosition(RXTXRobot.MOTOR2);
                    System.out.println("Encoder 1: " + distance_one + "\n Encoder 2: " + distance_two);
                }
                else{
                    r.sleep(3000);
                }
            }
            r.runEncodedMotor(RXTXRobot.MOTOR1, -255,ticks_to_turn_left, RXTXRobot.MOTOR2, -255, ticks_to_turn_right); // Turn left

            weather_sensors(r);
            phase_two = false;
        }
        
//        // open course
        if(phase_two == false){ 
            while(distance_one <=1400 && distance_two <=1400){
                ping = r.getPing(PING_PIN);
                System.out.println(ping);
            
            
                if(ping>=50){
                    r.runEncodedMotor(RXTXRobot.MOTOR1,500,150,RXTXRobot.MOTOR2,-500,150);
                    distance_one = r.getEncodedMotorPosition(RXTXRobot.MOTOR1);
                    distance_two = r.getEncodedMotorPosition(RXTXRobot.MOTOR2);
                    System.out.println("Encoder 1: " + distance_one + "\n Encoder 2: " + distance_two);
//                nav = check_obstacles(r,nav,wall);
//                if(nav[0] <=50 && nav[2] <=80 && nav[4]<=80){
//                    navigate_wall(r,nav,wall);
                }
                else{
                    navigate_obstacle(r,nav,wall);
                    wall  = true;
                }
            }
            
            do{
                r.refreshAnalogPins();
                r.runEncodedMotor(RXTXRobot.MOTOR1,500,150,RXTXRobot.MOTOR2,-500,150);

                AnalogPin bump = r.getAnalogPin(BUMP_PIN);
                reading = bump.getValue(); 
                System.out.println("Bump has value: " + reading);
                
                
            }
            while(reading >=10);
            
        }
//        
//        for(int x = 0; x < 100; x++){
//         System.out.println(r.getPing(PING_PIN));
//           
//         
//        r.runMotor(RXTXRobot.MOTOR2, 100, 1000);
        //SERVO CODE-----------------------------------------------------------
//        r.attachServo(RXTXRobot.SERVO1, 7);
//        r.moveServo(RXTXRobot.SERVO1,96);
//        r.sleep(3000);



        //BUMP CODE------------------------------------------------------------
//        r.attachMotor(RXTXRobot.MOTOR1,5);
//        r.attachMotor(RXTXRobot.MOTOR2,6);
//        do{
//           r.refreshAnalogPins();           
//           AnalogPin bump = r.getAnalogPin(BUMP_PIN);
//           reading = bump.getValue(); 
//           System.out.println("Bump has value: " + reading);
//           r.runMotor(RXTXRobot.MOTOR1, -450,RXTXRobot.MOTOR2, 237, 0);
//      
//        }
//        while(reading >=10);

        
        //PING CODE-----------------------------------------------------------
        
//            
//        while(game){
//            System.out.println(r.getPing(PING_PIN));
//            
//            if(r.getPing(PING_PIN)<=10){
//                System.out.println("Stop Motors");
//                r.runMotor(RXTXRobot.MOTOR1,0,0);
//                r.runMotor(RXTXRobot.MOTOR2, 0, 0);
//                game=false;
//            }
//        }
        
        
       //Checks ping sensor---------------------------------------
//       for (int x=0; x < 100; ++x) 
//		{ 
//			//Read the ping sensor value, which is connected to pin 12 
//			System.out.println("Response: " + r.getPing(PING_PIN) + " cm"); 
//			r.sleep(300); 
//		} 

        //TEMP SENSOR---------------------------------------------
//        for(int x=0; x<50; ++x){
//            //UNCERTAIN WHICH PIN IS WHICH
//            //READS TEMP VALUE, CONNECTED TO PIN 1
//            //READS WIND TEMP CONNECTED TO PIN 0
//            //Degrees after equation is in Celsius
//            //WindTemp Variable = tempCovered
//            //temperature variable = TempOpened
//            r.refreshAnalogPins();
//            AnalogPin tempSensor = r.getAnalogPin(0);
//            AnalogPin windSensor = r.getAnalogPin(1);
//            double windTemp = -.0956575*windSensor.getValue() +351.267-273;
//            double temperature = -.0956575*tempSensor.getValue() + 351.267-273;
//            double windSpeed = Math.pow(((windTemp-temperature)-35.74-(.6215*temperature))/(-35.75+.4275*temperature),(1/.16));
//            
//            System.out.println("WindTemp has value: " + windTemp);
//            r.sleep(1000);
//            System.out.println("Temp has value: " + temperature);
//            r.sleep(1000);
//            System.out.println("Windspeed has value: " + windSpeed);

            //SMART NAVIGATION-------------------------------------------------
            
             
        // Water sensor code
        
        // ANALOG PINS AT 4 AND 5
        // DIGITAL PINS AT 12 AND 13
//        for(int x = 0; x<100; x++){
//            int adc =  r.getConductivity();
////            double resistance = 2000*adc/(1-adc);
////            System.out.println(resistance);
//            System.out.println(adc);
//            
//        }
       	r.close(); 


    }
    public static int[] check_obstacles(RXTXRobot r, int nav[], boolean wall){
        nav[0] = r.getPing(PING_PIN);
        r.sleep(3000);
        nav[0] = r.getPing(PING_PIN);
        System.out.println(nav[0]);
        for (int x=1; x <= 2; ++x) 
		{ 
                    r.runEncodedMotor(RXTXRobot.MOTOR1,255,100, RXTXRobot.MOTOR2, 255,100);
                    nav[x] = r.getPing(PING_PIN);                   
                    //Read the ping sensor value, which is connected to pin 12 
                    System.out.println("Response: " + r.getPing(PING_PIN) + " cm"); 
		} 
        for (int x =1;x<=4;++x){
            r.runEncodedMotor(RXTXRobot.MOTOR1,-255,100, RXTXRobot.MOTOR2,-255,100);
            //Read the ping sensor value, which is connected to pin 12 
            System.out.println("Response: " + r.getPing(PING_PIN) + " cm");
            if(x==3 || x == 4){
                nav[x] = r.getPing(PING_PIN);
            }
        }
        for (int x=1; x <= 2; ++x) 
	{ 
            r.runEncodedMotor(RXTXRobot.MOTOR1,255,100, RXTXRobot.MOTOR2, 255,100);
            //Read the ping sensor value, which is connected to pin 12 
            System.out.println("Response: " + r.getPing(PING_PIN) + " cm"); 
	}
        return nav;
        //DETERMINE WHICH NAVIGATION TO DO
        
//        if(nav[2] <=80 && nav[4] <=80){
//            navigate_wall(r, nav, wall);
//        }
//
//        else{
//            navigate_obstacle(r,nav,wall);
//         }
       
        
    }
    public static void navigate_wall(RXTXRobot r, int nav[], boolean wall){
        if(nav[0] <=50 && wall == true){
            r.runEncodedMotor(RXTXRobot.MOTOR1, -255,ticks_to_turn_left, RXTXRobot.MOTOR2, -255, ticks_to_turn_right); // Turn left
            r.runEncodedMotor(RXTXRobot.MOTOR1, 500,100, RXTXRobot.MOTOR2, -500,100 );
        }
        else if(nav[0] <=50 && wall == false){
            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,ticks_to_turn_left, RXTXRobot.MOTOR2, 255, ticks_to_turn_right); // Turn Right
            r.runEncodedMotor(RXTXRobot.MOTOR1, 500,100, RXTXRobot.MOTOR2, -500, 100);
        }
//        else if(nav[2]<=50){
//            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,100, RXTXRobot.MOTOR2, -255,100);
//            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,100, RXTXRobot.MOTOR2, 255, 100);
//            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,100, RXTXRobot.MOTOR2, -255, 400);
//            r.runEncodedMotor(RXTXRobot.MOTOR1, -255,100, RXTXRobot.MOTOR2, -255, 100);
//                
//        }
//        else if(nav[5]<=50){
//             r.runEncodedMotor(RXTXRobot.MOTOR1, 255,100, RXTXRobot.MOTOR2, -255,100);
//            r.runEncodedMotor(RXTXRobot.MOTOR1, -255,100, RXTXRobot.MOTOR2, -255, 100);
//            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,100, RXTXRobot.MOTOR2, -255, 100);
//            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,100, RXTXRobot.MOTOR2, 255, 100);
//        }
        
        
        
    }
    public static void navigate_obstacle(RXTXRobot r, int nav[], boolean wall){
         if(nav[0] <=50 && wall == true){
            r.runEncodedMotor(RXTXRobot.MOTOR1, -255,ticks_to_turn_left, RXTXRobot.MOTOR2, -255, ticks_to_turn_right); // Turn left
            r.runEncodedMotor(RXTXRobot.MOTOR1, 500,400, RXTXRobot.MOTOR2, -500, 400);
            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,ticks_to_turn_left, RXTXRobot.MOTOR2, 255, ticks_to_turn_right); // Turn Right
            r.runEncodedMotor(RXTXRobot.MOTOR1, 500,400, RXTXRobot.MOTOR2, -500, 400);
            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,ticks_to_turn_left, RXTXRobot.MOTOR2, 255, ticks_to_turn_right); // Turn Right
            r.runEncodedMotor(RXTXRobot.MOTOR1, 500,400, RXTXRobot.MOTOR2, -500, 400);
            r.runEncodedMotor(RXTXRobot.MOTOR1, -255,ticks_to_turn_left, RXTXRobot.MOTOR2, -255, ticks_to_turn_right); // Turn left
            
 
        }
        else if(nav[0] <=50 && wall == false){
            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,ticks_to_turn_left, RXTXRobot.MOTOR2, 255, ticks_to_turn_right); // Turn Right
            r.runEncodedMotor(RXTXRobot.MOTOR1, 500,400, RXTXRobot.MOTOR2, -500, 400);
            r.runEncodedMotor(RXTXRobot.MOTOR1, -255,ticks_to_turn_left, RXTXRobot.MOTOR2, -255, ticks_to_turn_right); // Turn Left
            r.runEncodedMotor(RXTXRobot.MOTOR1, 500,400, RXTXRobot.MOTOR2, -500, 400);
            r.runEncodedMotor(RXTXRobot.MOTOR1, -255,ticks_to_turn_left, RXTXRobot.MOTOR2, -255, ticks_to_turn_right); // Turn Left
            r.runEncodedMotor(RXTXRobot.MOTOR1, 500,400, RXTXRobot.MOTOR2, -500, 400);
            r.runEncodedMotor(RXTXRobot.MOTOR1, 255,ticks_to_turn_left, RXTXRobot.MOTOR2, 255, ticks_to_turn_right); // Turn Right            
        }
    }
    public static void reset_encoders(RXTXRobot r){
        r.resetEncodedMotorPosition(RXTXRobot.MOTOR1);
        r.resetEncodedMotorPosition(RXTXRobot.MOTOR2);
    }
    public static void weather_sensors(RXTXRobot r){
        for(int x=0; x<50; ++x){
            //UNCERTAIN WHICH PIN IS WHICH
            //READS TEMP VALUE, CONNECTED TO PIN 1
            //READS WIND TEMP CONNECTED TO PIN 0
            //Degrees after equation is in Celsius
            //WindTemp Variable = tempCovered
            //temperature variable = TempOpened
            r.refreshAnalogPins();
            AnalogPin tempSensor = r.getAnalogPin(0);
            AnalogPin windSensor = r.getAnalogPin(1);
            double windTemp = -.0956575*windSensor.getValue() +351.267-273;
            double temperature = -.0956575*tempSensor.getValue() + 351.267-273;
            double windSpeed = Math.pow(((windTemp-temperature)-35.74-(.6215*temperature))/(-35.75+.4275*temperature),(1/.16));
            
            System.out.println("WindTemp has value: " + windTemp);
            r.sleep(1000);
            System.out.println("Temp has value: " + temperature);
            r.sleep(1000);
            System.out.println("Windspeed has value: " + windSpeed);
        }
    }
}

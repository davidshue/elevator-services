# elevator-challenge

Given a 100-story skyscraper, and it has 8 running elevators. This project simulates the operation of these elevators.

When you run the application, it will bootstrap a simulation of 200 random rides that are taking place. 100 of them going up, 100 of them going down. The console will print out the moving status of each elevator (8 of them)

Also every 20 seconds, the console will print out the snapshot of all 8 elevators. which level they are on, actions (up/down/idle)

On the console, any time you can enter your intention, as if you are pressing a call button outside the elevator:
20 up, means you are at 20th floor with the up intention
90 down, means you are at 90th floor with the down intention

The console will print out which one of the 8 elevators will be coming to you. The priority is determined by your call request (level/direction), plus the status of all 8 elevators. The elevator that has the highest priority (calculated inside the getPriority method in Elevator class) will be the one coming to you.

Also inside each elevator, it will also respond to floor request, and based on the current status of the elevator (level/direction), it will generate the floor queue accordingly). The closest level that follows the direction gets the highest precedence. For example, an elevator going down, currently at level 20, a button pressed at 19 will be the highest priority. On the other hand, a button pressed on 21 inside the elevator will have a much lower precedence, but still higher than a button pressed on 100.

The logic for the nextLevel is getNextStop method inside the Elevator class.

Also each elevator is a separate thread, and works rather independently. It will respond to a call request just as if someone presses the button inside through the CallSystem.

I did not tackle the stop/emergency call feature for the elevator. Also I assume it will take 2000 ms to go from one level to another level, regardless of the distance. This is just an assumption and did not account for human slowing down.

## build project
### gradle clean
### gradle fatJar
### cd build/libs
### java -jar elevator-services-0.0.1-SNAPSHOT.jar

## run the project
### get the snapshot

http://localhost:8080/snapshot

This will return the status of each elevator, also the level queue of each of them.

### call an elevator

http://localhost:8080/call?level=20&direction=down

The payload of this service will return a snapshot of all elevators, plus the one that is responding to your request.

### inside an elevator, press a button

http://localhost:8080/press?elevator=4&level=20

elevator is the elevator number that you are in, level is the button that you would have pressed. The service will return the payload of that
elevator's level queue

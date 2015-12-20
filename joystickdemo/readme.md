Simple util shows how to integrate joystick input device in Java. Just polls the controllers and renders their state to the console.

Uses Java 8, Maven

Tested on Ubuntu

Credits to Andrew Hoffman for the Jinput initialization procedure built into [halfnes](<https://github.com/andrew-hoffman/halfnes>)

My controllers are two 30 yr old Arcade joysticks. Applied the Retronic Design USB joystick adapter to connect them.

**Instructions**

Plug in your controllers / joysticks

	joystickdemo$> mvn package
	joystickdemo$> java -jar target/joystickdemo-0.1-jar-with-dependencies.jar
	
Now play around with the controllers and see what happens

	joystickdemo$> ctrl+c

**Resources**

* <https://github.com/jinput/jinput>
* <https://github.com/andrew-hoffman/halfnes>
* <http://www.retronicdesign.com/en/>

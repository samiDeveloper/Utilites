package bs.joystickdemo;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class JoystickDemo {
    public static void main(String[] args) {

        JInputHelper.setupJInput();
        
        Collection<Controller> controllers = findGameControllers();
        if (controllers.isEmpty()) {
            System.out.println("No controllers found, plug in your joystick");
            System.exit(0);
        }

        while (true) {
            StringBuffer sb = new StringBuffer();
            for (Controller c : controllers) {
                c.poll();
                sb.append(mkStringControllerState(c)).append("   ");
            }
            System.out.println(sb.toString());
            sleep(200);
        }
    }

    private static Collection<Controller> findGameControllers() {

        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        return Arrays.stream(controllers).filter(JoystickDemo::isGameController).collect(Collectors.toList());
    }

    private static boolean isGameController(Controller c) {
        return c.getType() == Controller.Type.STICK || c.getType() == Controller.Type.GAMEPAD
                || c.getType() == Controller.Type.WHEEL || c.getType() == Controller.Type.FINGERSTICK;

    }

    private static String mkStringControllerState(Controller c) {
        return Arrays.stream(c.getComponents()).map(Component::getPollData).map(f -> String.format("%.0f  ", f))
                .reduce("", (l, r) -> l + r);
    }

    private static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}

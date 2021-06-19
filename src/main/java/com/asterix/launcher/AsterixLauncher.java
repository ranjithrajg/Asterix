
package com.asterix.launcher;

import com.atom.launcher.AtomLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsterixLauncher extends AtomLauncher {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public AsterixLauncher() {
    }

    @Override
    public void doInit() {
        System.setProperty("atom.home", System.getProperty("server.home"));        //No I18N
    }

    @Override
    public void doStart() {
    }

    @Override
    public void doStop() {
    }

    public static void main(String args[]) throws Exception {
        AsterixLauncher launcher = new AsterixLauncher();
        launcher.init();
        launcher.start();
    }
}

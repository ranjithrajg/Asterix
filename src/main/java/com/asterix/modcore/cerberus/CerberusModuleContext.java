package com.asterix.modcore.cerberus;

import com.atom.commons.modz.*;
import com.atom.commons.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CerberusModuleContext extends AppModuleContext {

    public static final String CERBERUS_MODULE_KEY = "cerberus-module";        //No I18N

    private Settings cerberusSettings;
    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public CerberusModuleContext(AppContext appContext, boolean isPluggable, boolean isDistributable) throws Exception {
        super(appContext, AppModuleId.CERBERUS, isPluggable, isDistributable);
        loadDefaultSettingsFromFile(CERBERUS_MODULE_KEY);
    }

    public Settings cerberusSettings() {
        return cerberusSettings;
    }
}

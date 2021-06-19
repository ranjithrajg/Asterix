package com.asterix.modcore.store;

import com.atom.commons.modz.AppContext;
import com.atom.commons.modz.AppModuleContext;
import com.atom.commons.modz.AppModuleId;
import com.atom.commons.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoreModuleContext extends AppModuleContext {

    public static final String STORE_MODULE_KEY = "store-module";        //No I18N

    private Settings storeSettings;
    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public StoreModuleContext(AppContext appContext, boolean isPluggable, boolean isDistributable) throws Exception {
        super(appContext, AppModuleId.STORE, isPluggable, isDistributable);
        loadDefaultSettingsFromFile(STORE_MODULE_KEY);
    }

    public Settings storeSettings() {
        return storeSettings;
    }
}

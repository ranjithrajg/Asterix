package com.asterix.modcore.database;

import com.atom.commons.modz.AppContext;
import com.atom.commons.modz.AppModuleContext;
import com.atom.commons.modz.AppModuleId;
import com.atom.commons.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseModuleContext extends AppModuleContext {

    public static final String DATABASE_MODULE_KEY = "database-module";        //No I18N

    private Settings databaseSettings;

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public DatabaseModuleContext(AppContext appContext, boolean isPluggable, boolean isDistributable) throws Exception {
        super(appContext, AppModuleId.DATABASE, isPluggable, isDistributable);
        loadDefaultSettingsFromFile(DATABASE_MODULE_KEY);
    }

    public Settings databaseSettings() {
        return databaseSettings;
    }
}

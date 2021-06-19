package com.asterix.modcore.cache;

import com.atom.commons.modz.AppContext;
import com.atom.commons.modz.AppModuleContext;
import com.atom.commons.modz.AppModuleId;
import com.atom.commons.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheModuleContext extends AppModuleContext {

    public static final String CACHE_MODULE_KEY = "cache-module";        //No I18N

    private Settings cacheSettings;

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public CacheModuleContext(AppContext appContext, boolean isPluggable, boolean isDistributable) throws Exception {
        super(appContext, AppModuleId.CACHE, isPluggable, isDistributable);
        loadDefaultSettingsFromFile(CACHE_MODULE_KEY);
    }

    public Settings cacheSettings() {
        return cacheSettings;
    }
}


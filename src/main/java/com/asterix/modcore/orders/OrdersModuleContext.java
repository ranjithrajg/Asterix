package com.asterix.modcore.orders;

import com.atom.commons.modz.AppContext;
import com.atom.commons.modz.AppModuleContext;
import com.atom.commons.modz.AppModuleId;
import com.atom.commons.utils.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrdersModuleContext extends AppModuleContext {

    public static final String ORDERS_MODULE_KEY = "orders-module";        //No I18N

    private Settings ordersSettings;
    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public OrdersModuleContext(AppContext appContext, boolean isPluggable, boolean isDistributable) throws Exception {
        super(appContext, AppModuleId.ORDERS, isPluggable, isDistributable);
        loadDefaultSettingsFromFile(ORDERS_MODULE_KEY);
    }

    public Settings ordersSettings() {
        return ordersSettings;
    }
}

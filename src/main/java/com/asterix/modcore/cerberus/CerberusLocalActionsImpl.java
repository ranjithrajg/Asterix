package com.asterix.modcore.cerberus;

import com.atom.commons.modz.AppModuleClusterMetaInfo;
import com.asterix.modgateway.cerberus.CerberusLocalActions;

public class CerberusLocalActionsImpl implements CerberusLocalActions {

    @Override
    public AppModuleClusterMetaInfo getModuleMetaInfo() {
        return CerberusService.getInstance().getMetaInfo();
    }
}

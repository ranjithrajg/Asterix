package com.asterix.modcore.cerberus;

import com.asterix.modgateway.cerberus.CerberusGateway;
import com.atom.commons.modz.*;
import com.atom.commons.modz.Health;
import com.atom.modgateway.cluster.ClusterGateway;
import com.atom.commons.modz.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CerberusModule implements AppModule {

	private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

	public static final boolean IS_DISTRIBUTABLE = CerberusGateway.IS_DISTRIBUTABLE;
	public static final boolean IS_PLUGGABLE = CerberusGateway.IS_PLUGGABLE;

	private CerberusModuleContext moduleContext;
	private CerberusService cerberusService;

	@Override
	public AppModuleId moduleId() {
		return AppModuleId.CERBERUS;
	}

	@Override
	public void load(AppContext appContext) throws Exception {
		LOGGER.info("LOADING Cerberus Module...");   // NO I18N
		this.moduleContext = new CerberusModuleContext(appContext, IS_DISTRIBUTABLE, IS_PLUGGABLE);
		this.cerberusService = CerberusService.initInstance(appContext, moduleContext);
		registerModuleWithCluster();
		LOGGER.info("LOADED Cerberus Module!");   // NO I18N
	}

	@Override
	public void start() throws Exception {
		LOGGER.info("STARTING Cerberus Module...");   // NO I18N
		cerberusService.start();
		LOGGER.info("STARTED Cerberus Module!");   // NO I18N
	}

	@Override
	public void stop() throws Exception {
		LOGGER.info("STOPPING Cerberus Module...");   // NO I18N
		cerberusService.stop();
		LOGGER.info("STOPPED Cerberus Module!");   // NO I18N
	}

	@Override
	public void unload() throws Exception {
		LOGGER.info("UNLOADING Cerberus Module...");   // NO I18N
		cerberusService.close();
		LOGGER.info("UNLOADED Cerberus Module!");   // NO I18N
	}

	@Override
	public boolean isDistributable() {
		return IS_DISTRIBUTABLE;
	}

	@Override
	public boolean isPluggable() {
		return IS_PLUGGABLE;
	}

	@Override
	public Health health() {
		if(cerberusService.started()) {
			return Health.GREEN;
		} else if(cerberusService.initialized()) {
			return Health.YELLOW;
		} else {
			return Health.RED;
		}
	}

	@Override
	public AppModuleClusterMetaInfo getClusterMetaInfo() {
		return moduleContext.moduleClusterMetaInfo();
	}

	private void registerModuleWithCluster() throws Exception {
		ClusterGateway.local().registerLocalModule(moduleContext.moduleClusterMetaInfo());
	}
}

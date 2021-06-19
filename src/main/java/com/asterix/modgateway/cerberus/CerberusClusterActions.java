package com.asterix.modgateway.cerberus;

import com.asterix.modgateway.cerberus.actions.AuthenticateUserAction;
import com.atom.commons.action.ClusterActions;
import reactor.core.publisher.Mono;

public interface CerberusClusterActions extends ClusterActions {

    public Mono<AuthenticateUserAction.Response> authenticateUser(AuthenticateUserAction.Request request);
}
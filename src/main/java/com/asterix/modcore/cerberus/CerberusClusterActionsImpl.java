package com.asterix.modcore.cerberus;

import com.asterix.modgateway.cerberus.CerberusClusterActions;
import com.asterix.modgateway.cerberus.actions.AuthenticateUserAction;
import com.asterix.modgateway.database.DatabaseGateway;
import com.asterix.modcore.cerberus.db.CerberusQueryMapper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class CerberusClusterActionsImpl implements CerberusClusterActions {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    @Override
    public Mono<AuthenticateUserAction.Response> authenticateUser(AuthenticateUserAction.Request request) {
        String username = request.username.value();
        String password = request.password.value();

        System.out.println(">>>>>>>><< username : " + username);
        System.out.println(">>>>>>>><< password : " + password);

        AuthenticateUserAction.Response response;
        if(username.equals("admin") && password.equals("123")) {
            response = AuthenticateUserAction.AUTHENTICATE_USER.buildResponse()
                    .isAuthenticated.set(true)
                    .userId.set(12345)
                    .userName.set("asterix")
                    .name.set("Asterix")
                    .isAdmin.set(true);
        } else {
            response = AuthenticateUserAction.AUTHENTICATE_USER.buildResponse()
                    .isAuthenticated.set(false);
        }

        try (SqlSession session = DatabaseGateway.local().getMyBatisSqlSessionFactory().openSession()) {
            CerberusQueryMapper mapper = session.getMapper(CerberusQueryMapper.class);
            String password1 = mapper.selectUserPassword("astrix");
            System.out.println(">>>>>>>><< MyBatis : " + password1);
        }

        return Mono.just(response);
    }
}

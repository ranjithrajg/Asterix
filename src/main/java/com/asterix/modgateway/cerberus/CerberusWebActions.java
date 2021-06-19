package com.asterix.modgateway.cerberus;

import com.asterix.modgateway.cerberus.actions.AuthenticateUserAction;
import com.atom.modcore.connector.transport.http.vertx.VxRouteAddEventMsg;
import com.atom.modgateway.federal.FederalGateway;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CerberusWebActions {

    private static final Logger LOGGER = LoggerFactory.getLogger("Asterix");        //No I18N

    public static void registerWebRoutes() {
        FederalGateway.local().addRoute("/cerberus/authenticate-user", HttpMethod.POST, CerberusWebActions::authenticateUser, VxRouteAddEventMsg.newMsg().setAddDefaultBodyHandler(true));
    }

    public static void authenticateUser(RoutingContext context) {
        //String username = context.request().getParam("username");   // <1>

        JsonObject reqBody = context.getBodyAsJson();
        String username = reqBody.getString("username");   // <1>
        String password =  reqBody.getString("password");

        LOGGER.info(">>>>>>>> request() : " + context.request().params());
        LOGGER.info(">>>>>>>> headers() : " + context.request().headers());
        LOGGER.info(">>>>>>>> formAttributes() : " + context.request().formAttributes());
        LOGGER.info(">>>>>>>> cookieMap() : " + context.request().cookieMap());
        LOGGER.info(">>>>>>>> username : " + username);
        LOGGER.info(">>>>>>>> password : " + password);

        final JsonObject responseObj = new JsonObject();
        final JsonObject userObj = new JsonObject();
        try {
            AuthenticateUserAction.AUTHENTICATE_USER.buildRequest()
                    .username.set(username)
                    .password.set(password)
                    .executeDirectLocally()
                    .subscribe(
                            response -> {
                                if(response.isAuthenticated.get()) {
                                    responseObj.put("isAuthenticated", true);
                                    userObj.put("userId", response.userId.value());
                                    userObj.put("name", response.name.value());
                                    userObj.put("username", response.userName.value());
                                    userObj.put("isAdmin", response.isAdmin.value());
                                    responseObj.put("user", userObj);
                                } else {
                                    responseObj.put("isAuthenticated", false);
                                }
                            });
        } catch (Exception e) {
            LOGGER.error(">>>>>>>> error : " + e.getLocalizedMessage());
            e.printStackTrace();
        }

//        JWTAuthOptions config = new JWTAuthOptions()
//                .setKeyStore(new KeyStoreOptions()
//                        //.setPath("keystore.jceks")
//                        .setPassword("secret"));
//        JWTAuth provider = JWTAuth.create(context.vertx(), config);

        JWTAuth provider = JWTAuth.create(context.vertx(), new JWTAuthOptions()
                .addPubSecKey(new PubSecKeyOptions()
                        .setAlgorithm("HS256")
                        .setBuffer("keyboard cat")));

        String token = provider.generateToken(userObj, new JWTOptions());
        responseObj.put("token", token);

        HttpServerResponse response = context.response();
        response.putHeader("content-type", "application/json");
        response.putHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.putHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.putHeader("Access-Control-Allow-Headers", "X-CUSTOM, Content-Type");
        response.putHeader("Access-Control-Max-Age", "86400");
        response.end(responseObj.toString());
    }
}

package com.asterix.modgateway.cerberus.actions;

import com.asterix.modgateway.cerberus.CerberusClusterActions;
import com.asterix.modgateway.cerberus.CerberusGateway;
import com.atom.commons.action.ActionHandle;
import com.atom.commons.action.ActionRequest;
import com.atom.commons.action.ActionResponse;
import com.atom.commons.io.MessageInStream;
import com.atom.commons.io.MessageOutStream;
import com.atom.commons.io.Streamable;
import com.atom.commons.modz.AppModuleId;
import reactor.core.publisher.Mono;

import java.io.IOException;

public class AuthenticateUserAction {

    public static ActionHandle<Request, Response> AUTHENTICATE_USER = ActionHandle.build(
            "cerberus:/authenticate_user",       //No I18N
            AppModuleId.CERBERUS,
            Request::new,
            Response::new,
            AuthenticateUserAction::executeLocally);

    public static Mono<Response> executeLocally(Object localImpl, Request request) throws Exception {
        return ((CerberusClusterActions)localImpl).authenticateUser(request);
    }

    public static class Request extends ActionRequest<Response> {

        public final Streamable<String, Request> username = Streamable.stringType("username", this);       //No I18N
        public final Streamable<String, Request> password = Streamable.stringType("password", this);       //No I18N

        Request() {
            super(AUTHENTICATE_USER, CerberusGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream inStream) throws IOException {
            username.readFrom(inStream);
            password.readFrom(inStream);
        }

        @Override
        public void doWriteTo(MessageOutStream outStream) throws IOException {
            username.writeTo(outStream);
            password.writeTo(outStream);
        }

        @Override
        public String toString() {
            return super.toString() +
                    ", username:\"" + username.value() + "\"";       //No I18N
        }
    }

    public static class Response extends ActionResponse {

        public final Streamable<Boolean, Response> isAuthenticated = Streamable.boolType("is-authenticated", this);       //No I18N
        public final Streamable<Integer, Response> userId = Streamable.intType("user-id", this);       //No I18N
        public final Streamable<String, Response> userName = Streamable.stringType("user-name", this);       //No I18N
        public final Streamable<String, Response> name = Streamable.stringType("name", this);       //No I18N
        public final Streamable<Boolean, Response> isAdmin = Streamable.boolType("is-admin", this);       //No I18N
        public final Streamable<Long, Response> sessionId = Streamable.longType("session-id", this);       //No I18N

        public Response() {
            super(AUTHENTICATE_USER, CerberusGateway.router());
        }

        @Override
        public void doReadFrom(MessageInStream inStream) throws IOException {
            isAuthenticated.readFrom(inStream);
            userId.readFrom(inStream);
            userName.readFrom(inStream);
            name.readFrom(inStream);
            isAdmin.readFrom(inStream);
            sessionId.readFrom(inStream);
        }

        @Override
        public void doWriteTo(MessageOutStream outStream) throws IOException {
            isAuthenticated.writeTo(outStream);
            userId.writeTo(outStream);
            userName.writeTo(outStream);
            name.writeTo(outStream);
            isAdmin.writeTo(outStream);
            sessionId.writeTo(outStream);
        }

        @Override
        public String toString() {
            return super.toString() +
                    ", is-authenticated:\"" + isAuthenticated.value() + "\"" +       //No I18N
                    ", user-id:\"" + userId.value() + "\"" +       //No I18N
                    ", user-name:\"" + userName.value() + "\"" +       //No I18N
                    ", name:\"" + name.value() + "\"" +       //No I18N
                    ", is-admin:\"" + isAdmin.value() + "\"" +       //No I18N
                    ", session-id : " + sessionId.value();       //No I18N
        }
    }
}

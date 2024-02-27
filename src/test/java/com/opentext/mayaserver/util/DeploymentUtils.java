package com.opentext.mayaserver.util;

import static com.opentext.mayaserver.util.Constants.*;

public class DeploymentUtils {


    public static String buildCreateUseCaseURL(int port) {
        String getCreateUseCaseURI = BASE_URI + "/" + USE_CASE;
        return formFullURLWithPort(port, getCreateUseCaseURI);
    }

    public static String buildDeleteUseCaseURL(int port) {
        String getDeleteUseCaseURI = BASE_URI + "/" + USE_CASE;
        return formFullURLWithPort(port, getDeleteUseCaseURI);
    }

    public static String buildGetUseCaseURL(int port) {
        String getUseCaseURI = BASE_URI + "/" + USE_CASE;
        return formFullURLWithPort(port, getUseCaseURI);
    }

    private static String formFullURLWithPort(int port, String uri) {
        return HOST + port + CONTEXT_PATH + uri;
    }
}

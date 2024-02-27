package com.opentext.mayaserver.models;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rajiv
 */
public enum StateEnum {

    SUBMITTED,
    IN_PROGRESS,
    FAILED,
    CREATED;

    private static final List<StateEnum> DELETE_USE_CASE_ALLOWED_LIST = Arrays.asList(StateEnum.CREATED, StateEnum.FAILED);
    private static final List<StateEnum> DELETE_USE_CASE_NOT_ALLOWED_LIST = Arrays.asList(StateEnum.SUBMITTED, StateEnum.IN_PROGRESS);

    public static boolean isUseCaseDeletionAllowed(StateEnum stateEnum) {
        return DELETE_USE_CASE_ALLOWED_LIST.contains(stateEnum);
    }
}

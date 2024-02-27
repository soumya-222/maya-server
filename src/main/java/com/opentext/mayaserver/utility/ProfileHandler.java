package com.opentext.mayaserver.utility;

import lombok.Data;

/**
 * @author Rajiv
 */
@Data
public class ProfileHandler {
    boolean isLocalOrTestProfileActive;
    boolean isProductionProfileActive;
}

package com.opentext.mayaserver.environments.mockoon;

/**
 * @author Soumyaranjan
 */

public interface ConfigFileCreator {
     void prepareAccountConfigFile(String useCaseName);
     void prepareRecommendationConfigFile(String useCaseName);
}

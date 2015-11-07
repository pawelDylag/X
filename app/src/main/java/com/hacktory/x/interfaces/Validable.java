package com.hacktory.x.interfaces;

/**
 * Created by lukasz on 07.11.15.
 */
public interface Validable {
    /**
     * @param levelOfSecurityBroken - 0,1,2,3,4 levels of security
     */
    void onValidationSuccess(int levelOfSecurityBroken);

    /**
     * when validation in progress, if user goes with invalid path, this method is invoked
     */
    void onValidationFailed();

    void onSequenceRestart();

}

package org.kilkaari.library.application;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by vaibhav on 30/05/14.
 */
public class Prefs {
    /**
     * The Constant sharedPrefsName.
     */
    private static final String sharedPrefsName = "AppPrefs";


    private static final String INSTALLATION_NURSERY_ID = "nurseryId";
    private static final String INSTALLATION_NURSERY_NAME = "nurseryName";
    private static final String INSTALLATION_NURSERY_CHAIN_ID = "nurseryChainId";
    private static final String INSTALLATION_NURSERY_CHAIN_NAME = "nurseryChainName";

    /* ---  preferences of application --- */
    private static final String EYLOG_API_KEY = "apiKey";
    private static final String EYLOG_API_PASSWORD = "apiPassword";
    private static final String EYLOG_API_PASSWORD_ENCODED = "apiPasswordEncoded";
    private static final String EYLOG_PRACTITIONER_PIN = "practitionerPin";
    private static final String EYLOG_PRACTITIONER_PIN_ENCODED = "practitionerPinEncoded";
    private static final String EYLOG_PRACTITIONER_ID = "practitionerId";


    private static final String OBSERVATION_IS_EDIT = "observationIsEdit";

   /*--- Daily diary , settings and label objects of json ----*/
    private static final String INSTALLATION_DIARY_FIELDS = "installationDiaryFields";
    private static final String INSTALLATION_SETTINGS = "installationSettings";
    private static final String INSTALLATION_LABEL = "installationLabel";


    /*--- URL of images ---*/
    private static final String INSTALLATION_CHILDREN_PHOTO_URL= "childrenPhotoUrl";
    private static final String INSTALLATION_PRACTITIONER_PHOTO_URL= "practitionerPhotoUrl";

    /*--- Application Preferences --*/

    private static final String IS_LOGGED_IN = "isLogggedIn";
    private static final String IS_DATA_SAVED = "isDataSaved";
    private static final String IS_PRACTITIONER_PHOTO_SAVED = "isDataSaved";
    private static final String IS_CHILDREN_PHOTO_SAVED = "isDataSaved";

    /**
     * The m_context.
     */
    private Context context;

    /**
     * Instantiates a new prefs.
     *
     * @param context the context
     */
   public Prefs(Context context) {
        this.context = context;
    }

    /**
     * Gets the.
     *
     * @return the shared preferences
     */
    private SharedPreferences get() {
        return context.getSharedPreferences(sharedPrefsName,
                Context.MODE_PRIVATE);
    }


    public String getInstallationNurseryId() {
        return get().getString(INSTALLATION_NURSERY_ID, null);
    }

    public void setInstallationNurseryId(String installationNurseryId) {
        get().edit().putString(INSTALLATION_NURSERY_ID, installationNurseryId).commit();
    }

    public String getInstallationNurseryName() {
        return get().getString(INSTALLATION_NURSERY_NAME, null);
    }

    public void setInstallationNurseryName(String installationNurseryName) {
        get().edit().putString(INSTALLATION_NURSERY_NAME, installationNurseryName).commit();
    }

    public String getInstallationNurseryChainId() {
        return get().getString(INSTALLATION_NURSERY_CHAIN_ID, null);
    }

    public void setInstallationNurseryChainId(String installationNurseryChainId) {
        get().edit().putString(INSTALLATION_NURSERY_CHAIN_ID, installationNurseryChainId).commit();
    }

    public String getInstallationNurseryChainName() {
        return get().getString(INSTALLATION_NURSERY_CHAIN_NAME, null);
    }

    public void setInstallationNurseryChainName(String installationNurseryChainName) {
        get().edit().putString(INSTALLATION_NURSERY_CHAIN_NAME, installationNurseryChainName).commit();
    }
    public String getInstallationDiaryFields() {
        return get().getString(INSTALLATION_DIARY_FIELDS, null);
    }

    public void setInstallationDiaryFields(String installationDiaryFields) {
        get().edit().putString(INSTALLATION_DIARY_FIELDS, installationDiaryFields).commit();
    }

    public String getInstallationSettings() {
        return get().getString(INSTALLATION_SETTINGS, null);
    }

    public void setInstallationSettings(String installationSettings) {
        get().edit().putString(INSTALLATION_SETTINGS, installationSettings).commit();
    }
    public String getInstallationLabel() {
        return get().getString(INSTALLATION_LABEL, null);
    }

    public void setInstallationLabel(String installationLabel) {
        get().edit().putString(INSTALLATION_LABEL, installationLabel).commit();
    }

    public String getInstallationChildrenPhotoUrl() {
        return get().getString(INSTALLATION_CHILDREN_PHOTO_URL, null);
    }

    public void setInstallationChildrenPhotoUrl(String installationChildrenPhotoUrl) {
        get().edit().putString(INSTALLATION_CHILDREN_PHOTO_URL, installationChildrenPhotoUrl).commit();
    }

    public String getInstallationPractitionerPhotoUrl() {
        return get().getString(INSTALLATION_PRACTITIONER_PHOTO_URL, null);
    }

    public void setInstallationPractitionerPhotoUrl(String installationPractitionerPhotoUrl) {
        get().edit().putString(INSTALLATION_PRACTITIONER_PHOTO_URL, installationPractitionerPhotoUrl).commit();
    }

    public boolean isLoggedIn() {
        return get().getBoolean(IS_LOGGED_IN,false);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        get().edit().putBoolean(IS_LOGGED_IN,isLoggedIn).commit();
    }
    public boolean isDataSaved() {
        return get().getBoolean(IS_DATA_SAVED,false);
    }

    public void setIsDataSaved(boolean isDataSaved) {
        get().edit().putBoolean(IS_DATA_SAVED,isDataSaved).commit();
    }
    public boolean isPractitionerPhotoSaved() {
        return get().getBoolean(IS_PRACTITIONER_PHOTO_SAVED,false);
    }

    public void setIsPractitionerPhotoSaved(boolean isPractitionerPhotoSaved) {
        get().edit().putBoolean(IS_PRACTITIONER_PHOTO_SAVED,isPractitionerPhotoSaved).commit();
    }
    public boolean isChildrenPhotoSaved() {
        return get().getBoolean(IS_CHILDREN_PHOTO_SAVED,false);
    }

    public void setIsChildrenPhotoSaved(boolean isChildrenPhotoSaved) {
        get().edit().putBoolean(IS_CHILDREN_PHOTO_SAVED,isChildrenPhotoSaved).commit();
    }

    public String getEylogApiKey() {
        return get().getString(EYLOG_API_KEY, null);
    }

    public void setEylogApiKey(String eylogApiKey) {
        get().edit().putString(EYLOG_API_KEY, eylogApiKey).commit();
    }
    public String getEylogApiPassword() {
        return get().getString(EYLOG_API_PASSWORD, null);
    }

    public void setEylogApiPassword(String apiPassword) {
        get().edit().putString(EYLOG_API_PASSWORD, apiPassword).commit();
    }
    public String getEylogApiPasswordEncoded() {
        return get().getString(EYLOG_API_PASSWORD_ENCODED, null);
    }

    public void setEylogApiPasswordEncoded(String apiPasswordEncoded) {
        get().edit().putString(EYLOG_API_PASSWORD_ENCODED, apiPasswordEncoded).commit();
    }
    public String getEylogPractitionerPin() {
        return get().getString(EYLOG_PRACTITIONER_PIN, null);
    }

    public void setEylogPractitionerPin(String practitionerPin) {
        get().edit().putString(EYLOG_PRACTITIONER_PIN, practitionerPin).commit();
    }

    public String getEylogPractitionerPinEncoded() {
        return get().getString(EYLOG_PRACTITIONER_PIN_ENCODED, null);
    }

    public void setEylogPractitionerPinEncoded(String practitionerPinEncoded) {
        get().edit().putString(EYLOG_PRACTITIONER_PIN_ENCODED, practitionerPinEncoded).commit();
    }

    public String getEylogPractitionerId() {
        return get().getString(EYLOG_PRACTITIONER_ID, null);
    }

    public void setEylogPractitionerId(String practitionerId) {
        get().edit().putString(EYLOG_PRACTITIONER_ID, practitionerId).commit();
    }
    public boolean isObservationEdit() {
        return get().getBoolean(OBSERVATION_IS_EDIT,false);
    }

    public void setObservationIsEdit(boolean observationIsEdit) {
        get().edit().putBoolean(OBSERVATION_IS_EDIT,observationIsEdit).commit();
    }

}

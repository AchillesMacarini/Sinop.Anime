package com.teknestige.classes;

import DbControler.BDHelper;

public class Constants {
    static BDHelper bdHelper;

    public static final String UPLOAD_URL = bdHelper.returnUrl() + "upload.php";
    public static final String IMAGES_URL = bdHelper.returnUrl() + "getImages.php";
}

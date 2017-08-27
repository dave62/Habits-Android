package com.github.dave62.habits.constants;

import java.text.SimpleDateFormat;
import java.util.Locale;

//TODO : Find a way to get rid of this kind of class
public class Constants {

    //We prefer to have a defined format when it's just about passing dates into a bundle
    public final static SimpleDateFormat DATE_FORMAT_FOR_BUNDLE = new SimpleDateFormat("MM/dd/yy", Locale.US);
}

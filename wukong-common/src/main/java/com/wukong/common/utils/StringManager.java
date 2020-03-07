package com.wukong.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JemmyZhang on 2018/2/26
 */
public class StringManager implements Serializable{

    private static final long serialVersionUID = 2512721192752241733L;

    private static transient Logger log = LoggerFactory.getLogger(StringManager.class);

    private static final String DEFAULT_PATH = "messages.";

    private String bundleName = null;

    private Locale locale = null;

    private ClassLoader loader = null;

    private transient ResourceBundle bundle;

    private static Map<String, Map<Locale, StringManager>> bundleManagers = new ConcurrentHashMap<>();

    private StringManager(String bundleName, Locale locale, ClassLoader loader) {
        this.bundleName = bundleName;
        this.locale = locale;
        this.loader = loader;
        initResourceBundle();
    }

    private void initResourceBundle() {
        try {
            if (this.loader == null) {
                this.bundle = ResourceBundle.getBundle(this.bundleName, this.locale);
            } else {
                this.bundle = ResourceBundle.getBundle(this.bundleName, this.locale, this.loader);
            }
        } catch (MissingResourceException ex) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader != null) {
                try {
                    this.bundle = ResourceBundle.getBundle(this.bundleName, this.locale, classLoader);
                } catch (MissingResourceException ex2) {
                    log.info(null, ex2);
                }
            }

            ClassLoader cl2 = this.getClass().getClassLoader();

            try {
                this.bundle = ResourceBundle.getBundle(this.bundleName, this.locale, cl2);
            } catch (MissingResourceException ex3) {
                log.info(null, ex3);
            }

            if (this.bundle != null) {
                return;
            }
            log.warn("Can not find resource for bundle " + this.bundleName);
        }
    }

    protected String getStringInternal(String key) {
        if (key == null) {
            throw new NullPointerException("key is null");
        }
        if (this.bundle == null) {
            return key;
        }
        String str;
        try {
            str = this.bundle.getString(key);
        } catch (MissingResourceException ex) {
            str = getMissingValue(key);
            if (log.isDebugEnabled()) {
                log.debug("Cannot find message associated with key \'" + key + "\'");
            }
        }
        return str;
    }

    public String getString(String key) {
        return this.getStringInternal(key);
    }

    public String getString(String key, Object... args) {
        return this.formatMessage(this.getString(key), args);
    }

    private String formatMessage(String value, Object... args) {
        String iString;
        if (args != null) {
            for (int iae = 0; iae < args.length; ++iae) {
                if (args[iae] instanceof Number) {
                    args[iae] = String.valueOf(args[iae]);
                }
            }
        }

        try {
            iString = MessageFormat.format(value, args);
        } catch (IllegalArgumentException var7) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            if (args != null) {
                for (int i = 0; i < args.length; ++i) {
                    sb.append(" arg[").append(i).append("]=").append(args[i]);
                }
            }

            iString = sb.toString();
            log.warn("Message format error: " + iString);
        }

        return iString;
    }

    public static StringManager getManager(Class<?> clazz) {
        return getManagerWithBundleName(DEFAULT_PATH + clazz.getSimpleName());
    }

    public static StringManager getManager(String resourceName) {
        return getManagerWithBundleName(DEFAULT_PATH + resourceName);
    }

    public static StringManager getManager(Class<?> clazz, Locale locale) {
        return getManagerWithBundleName(DEFAULT_PATH + clazz.getSimpleName(), locale);
    }

    public static StringManager getManager(String resourceName, Locale locale) {
        return getManagerWithBundleName(DEFAULT_PATH + resourceName, locale);
    }

    public static StringManager getManagerWithBundleName(String bundleName) {
        return getManagerWithBundleName(bundleName, Locale.getDefault());
    }

    public static StringManager getManagerWithBundleName(String bundleName, Locale locale) {
        return getManagerWithBundleName(bundleName, locale, null);
    }

    public static StringManager getManagerWithBundleName(String bundleName, Locale locale, ClassLoader loader) {
        Map<Locale, StringManager> mgrs = bundleManagers.get(bundleName);
        if (mgrs == null) {
            mgrs = new ConcurrentHashMap<>();
            bundleManagers.put(bundleName, mgrs);
        }

        StringManager mgr = mgrs.get(locale);
        if (mgr == null) {
            mgr = new StringManager(bundleName, locale, loader);
            if (mgr.bundle != null) {
                mgrs.put(locale, mgr);
            }
        }
        return mgr;
    }

    private static String getMissingValue(String key) {
        return "?" + key + "?";
    }

    public static boolean isMissingValue(String key, String value) {
        return ("?" + key + "?").equals(value);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.initResourceBundle();
    }
}

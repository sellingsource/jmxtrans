package com.googlecode.jmxtrans.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.pool.KeyedObjectPool;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.googlecode.jmxtrans.OutputWriter;

/**
 * Implements the common code for output filters.
 *
 * @author jon
 */
public abstract class BaseOutputWriter implements OutputWriter {

    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String OUTPUT_FILE = "outputFile";
    public static final String TEMPLATE_FILE = "templateFile";
    public static final String BINARY_PATH = "binaryPath";
    public static final String DEBUG = "debug";
    public static final String TYPE_NAMES = "typeNames";

    private Boolean debugEnabled = null;
    private Map<String, Object> settings;

    /** */
    public void addSetting(String key, Object value) {
        getSettings().put(key, value);
    }

    /** */
    public Map<String, Object> getSettings() {
        if (this.settings == null) {
            this.settings = new TreeMap<String, Object>();
        }
        return this.settings;
    }

    /** */
    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
        PropertyResolver.resolveMap(this.settings);
    }

    /** */
    public boolean getBooleanSetting(String key) {
        Boolean result = null;
        if (this.getSettings().containsKey(key)) {
            Object foo = this.getSettings().get(key);
            if (foo instanceof String) {
                result = Boolean.valueOf((String)foo);
            } else if (foo instanceof Boolean) {
                result = (Boolean)foo;
            }
        }
        return result != null ? result : false;
    }

    /** */
    @JsonIgnore
    public boolean isDebugEnabled() {
        if (debugEnabled == null) {
            return getBooleanSetting(DEBUG);
        }
        return debugEnabled != null ? debugEnabled : false;
    }

    /** */
    public void setTypeNames(List<String> typeNames) {
        this.getSettings().put(TYPE_NAMES, typeNames);
    }

    /** */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public List<String> getTypeNames() {
        if (!this.getSettings().containsKey(TYPE_NAMES)) {
            List<String> tmp = new ArrayList<String>();
            this.getSettings().put(TYPE_NAMES, tmp);
        }
        return (List<String>) this.getSettings().get(TYPE_NAMES);
    }

    /** */
    public void addTypeName(String str) {
        this.getTypeNames().add(str);
    }

    /**
     * Given a typeName string, get the first match from the typeNames setting.
     * In other words, suppose you have:
     *
     * typeName=name=PS Eden Space,type=MemoryPool
     *
     * If you addTypeName("name"), then it'll retrieve 'PS Eden Space' from the string
     */
    protected String getConcatedTypeNameValues(String typeNameStr) {
        return JmxUtils.getConcatedTypeNameValues(this.getTypeNames(), typeNameStr);
    }

    /**
     * Replaces all . with _ and removes all spaces and double/single quotes.
     */
    protected String cleanupStr(String name) {
        return JmxUtils.cleanupStr(name);
    }

    /**
     * A do nothing method.
     */
    @Override
    public void setObjectPoolMap(Map<String, KeyedObjectPool> poolMap) {
        // Do nothing.
    }

    /**
     * A do nothing method.
     */
    @Override
    public void start() throws LifecycleException {
        // Do nothing.
    }

    /**
     * A do nothing method.
     */
    @Override
    public void stop() throws LifecycleException {
        // Do nothing.
    }
}

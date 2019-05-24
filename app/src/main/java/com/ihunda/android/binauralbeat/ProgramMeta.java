package com.ihunda.android.binauralbeat;

import java.lang.reflect.Method;

public class ProgramMeta {

    public enum Category {
        HYPNOSIS,
        SLEEP,
        HEALING,
        LEARNING,
        MEDITATION,
        STIMULATION,
        OOBE,
        CP,
        OTHER
    }

    private Method method;
    private String name;
    private Category cat;
    private CategoryGroup group;

    public ProgramMeta(Method method, String name, Category cat) {
        this.method = method;
        this.name = name;
        this.cat = cat;
        this.group = null;
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    public Category getCat() {
        return cat;
    }

    public CategoryGroup getGroup() {
        return group;
    }

    public void setGroup(CategoryGroup group) {
        this.group = group;
    }
}

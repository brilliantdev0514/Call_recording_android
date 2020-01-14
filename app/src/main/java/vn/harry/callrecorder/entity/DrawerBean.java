package vn.harry.callrecorder.entity;


public class DrawerBean {
    private int resId;
    private String title;
    private int type;

    public DrawerBean(int resId, String title, int type) {
        this.resId = resId;
        this.title = title;
        this.type = type;
    }

    public int getResId() {
        return resId;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }
}

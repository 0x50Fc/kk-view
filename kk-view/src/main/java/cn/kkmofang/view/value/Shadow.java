package cn.kkmofang.view.value;

/**
 * Created by zhanghailong on 2018/5/8.
 */

public class Shadow implements IValue<Shadow>{

    public final Pixel x = new Pixel();
    public final Pixel y = new Pixel();
    public final Pixel radius = new Pixel();
    public int color;

    @Override
    public void set(String value) {

        if(value == null) {
            return;
        }

        String[] vs = value.split(" ");

        if(vs.length >= 4) {
            x.set(vs[0]);
            x.set(vs[1]);
            radius.set(vs[2]);
            color = Color.valueOf(vs[3],0);
        }
    }

    @Override
    public void set(Shadow value) {
        x.set(value.x);
        y.set(value.y);
        radius.set(value.radius);
        color = value.color;
    }

}

package cn.kkmofang.view.value;

/**
 * Created by hailong11 on 2018/1/17.
 */

public final class Edge implements IValue<Edge> {

    public final Pixel top = new Pixel();
    public final Pixel right = new Pixel();
    public final Pixel bottom = new Pixel();
    public final Pixel left = new Pixel();

    @Override
    public void set(String v) {

        if(v != null) {

            String[] vs = v.split(" ");

            if(vs.length > 0) {

                top.set(vs[0]);

                if(vs.length > 1) {

                    right.set(vs[1]);

                    if(vs.length > 2) {

                        bottom.set(vs[2]);

                        if(vs.length > 3) {
                            left.set(vs[3]);
                        } else {
                            left.set(right);
                        }

                    } else {
                        bottom.set(top);
                        left.set(right);
                    }

                } else {
                    right.set(top);
                    bottom.set(bottom);
                    left.set(left);
                }
            }
        }

    }

    @Override
    public void set(Edge v) {
        top.set(v.top);
        right.set(v.right);
        bottom.set(v.bottom);
        left.set(v.left);
    }


}

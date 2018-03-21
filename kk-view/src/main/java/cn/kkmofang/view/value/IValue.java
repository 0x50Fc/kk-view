package cn.kkmofang.view.value;

/**
 * Created by hailong11 on 2018/1/17.
 */
//整数
public interface IValue<T extends IValue<T> > {

    public String toString();

    public void set(String value);

    public void set(T value);

}

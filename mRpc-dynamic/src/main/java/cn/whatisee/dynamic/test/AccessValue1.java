package cn.whatisee.dynamic.test;

/**
 * Created by ming on 2016/12/16.
 */
public class AccessValue1 implements IAccess {
    private HolderBean m_target;

    @Override
    public void setTarget(Object target) {
        m_target = (HolderBean)target;
    }

    @Override
    public int getValue() {
        return m_target.getValue1();
    }

    @Override
    public void setValue(int value) {
        m_target.setValue1(value);
    }
}

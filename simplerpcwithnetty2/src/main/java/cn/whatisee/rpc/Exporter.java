package cn.whatisee.rpc;

/**
 * Exporter. (API/SPI, Prototype, ThreadSafe)
 *
 * @author william.liangf
 */
public interface Exporter<T> {

    /**
     * get invoker.
     *
     * @return invoker
     */
    Invoker<T> getInvoker();

    /**
     * unexport.
     * <p>
     * <code>
     * getInvoker().destroy();
     * </code>
     */
    void unexport();

}

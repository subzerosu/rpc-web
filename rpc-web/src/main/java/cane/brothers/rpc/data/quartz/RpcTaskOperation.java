package cane.brothers.rpc.data.quartz;

public enum RpcTaskOperation {
    UNDEFINED,
    START,
    PAUSE,
    STOP;

    @Override
    public String toString() {
        return name().toLowerCase();
    };
}

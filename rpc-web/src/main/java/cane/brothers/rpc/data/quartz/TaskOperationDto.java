package cane.brothers.rpc.data.quartz;

public class TaskOperationDto {

    private String operation;

    /**
     * Default constructor
     */
    public TaskOperationDto() {
        super();
    }

    /**
     * Constructor
     */
    public TaskOperationDto(String oper) {
        this.operation = oper;
    }

    /**
     * Constructor
     */
    public TaskOperationDto(RpcTaskOperation rpcOper) {
        this(rpcOper.toString());
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TaskOperationDto [operaion=").append(operation).append("]");
        return builder.toString();
    }

}

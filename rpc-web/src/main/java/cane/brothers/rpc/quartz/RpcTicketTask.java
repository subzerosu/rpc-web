package cane.brothers.rpc.quartz;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author cane
 */
@Component("task")
public class RpcTicketTask implements Serializable {

	private static final long serialVersionUID = -8586754748391351562L;

	private static final Logger log = LoggerFactory.getLogger(RpcTicketTask.class);

	// TODO
	public void print() {
		log.info("Executing job.......");
	}
}

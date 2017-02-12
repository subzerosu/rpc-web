package cane.brothers.rpc.service.email;

/**
 * Сервис отправки e-mail, содержащих отчет о проверке ПО.
 *
 * @author cane
 */
public interface RpcMail {

	boolean sendEmail(String output);
}

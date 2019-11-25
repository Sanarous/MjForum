package cn.bestzuo.zuoforum.service;

import javax.mail.MessagingException;

/**
 * 调用JavaMailSender接口发送邮件
 */
public interface IMailService {
    /**
     * 发送文本邮件
     * @param to
     * @param subject
     * @param content
     */
    void sendSimpleMail(String to, String subject, String content);

    void sendSimpleMail(String to, String subject, String content, String... cc);

    /**
     * 发送HTML邮件
     * @param to
     * @param subject
     * @param content
     * @throws MessagingException
     */
    void sendHtmlMail(String to, String subject, String content) throws MessagingException;

    void sendHtmlMail(String to, String subject, String content, String... cc);

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param filePath
     * @throws MessagingException
     */
    void sendAttachmentsMail(String to, String subject, String content, String filePath) throws MessagingException;

    void sendAttachmentsMail(String to, String subject, String content, String filePath, String... cc);

    /**
     * 发送正文中有静态资源的邮件
     * @param to
     * @param subject
     * @param content
     * @param rscPath
     * @param rscId
     * @throws MessagingException
     */
    void sendResourceMail(String to, String subject, String content, String rscPath, String rscId) throws MessagingException;

    void sendResourceMail(String to, String subject, String content, String rscPath, String rscId, String... cc);

}

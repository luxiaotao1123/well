package org.wella.common.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil implements Runnable {
	private String email;// 收件人邮箱
	private String content;// 激活码

	public MailUtil(String email, String content) {
		this.email = email;
		this.content = content;
	}

	public void run() {
		// 1.创建连接对象javax.mail.Session
		// 2.创建邮件对象 javax.mail.Message
		// 3.发送一封激活邮件
		String from = ConstantUtil.SEND_EMAIL_FROM;// 发件人电子邮箱
		String host = ConstantUtil.SEND_EMAIL_HOST; // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)
		//String from = "jianwending@wellassist.com.cn";
		//String host = "smtp.exmail.qq.com";

		Properties properties = System.getProperties();// 获取系统属性

		properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
		properties.setProperty("mail.smtp.auth", "true");// 打开认证

		try {
			//QQ邮箱需要下面这段代码，163邮箱不需要
			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			properties.put("mail.smtp.ssl.enable", "true");
			properties.put("mail.smtp.ssl.socketFactory", sf);
			
			
			// 1.获取默认session对象
			/*Session session = Session.getDefaultInstance(properties, new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("15824275713@163.com", "shjgpslw880219"); // 发件人邮箱账号、授权码
				}
			});*/
			/*Session session = Session.getDefaultInstance(properties, new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("jianwending@wellassist.com.cn", "Jianwen123"); // 发件人邮箱账号、授权码
				}
			});*/
			Session session = Session.getDefaultInstance(properties, new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(ConstantUtil.SEND_EMAIL_FROM, ConstantUtil.SEND_EMAIL_PASS); // 发件人邮箱账号、授权码
				}
			});

			// 2.创建邮件对象
			Message message = new MimeMessage(session);
			// 2.1设置发件人
			message.setFrom(new InternetAddress(from));
			// 2.2设置接收人
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			// 2.3设置邮件主题
			message.setSubject("账号激活");
			// 2.4设置邮件内容
			message.setContent(content, "text/html;charset=UTF-8");
			// 3.发送邮件
			Transport.send(message);
			System.out.println("邮件成功发送!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

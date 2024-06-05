package com.kivy0000.utils;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 1.这是一个个工具类,以后将会被 mailStarter+properties代替并重构
 * 注意：你需要使用MailUtilConfig并配置application.yml/properties的发件人和授权码令其初始化后才可以使用
 *      为了保证安全性和可维护性，推荐这样使用，你也可以直接调用setDefaultSender/setDefaultPassword进行初始化
 * --------------------
 * 2.返回 验证码/"failed" 字符串代表成功/失败
 * 3.目前支持126，QQ，163,gmail,sina,sohu邮箱的smtp协议
 */
@SuppressWarnings("unused")
//TODO 在提交时，还原application.yml中的敏感信息
public class MailUtil {

    //初始化通用的配置属性
    private static final Properties props = createProperties();
    //默认传输协议
    private static final String protocol = "smtp";
    //默认发件人账号
    private static String DefaultSender;
    //smtp协议的授权码，(若是公司自定义服务器，可无需授权码，但可能需要配置证书)
    private static String DefaultPassword;
    //默认发件服务端
    private static final String DefaultHost = "smtp.126.com";
    //端口库
    private static final String[] PORTS = {"587", "465", "993", "995", "25", "110", "143", "993"};
    //服务端库
    private static final Map<String, String> Hosts = createHosts();


    /**
     * 发送验证码邮件的方法
     */
    public static String sendVcode(String receiver) {
        return send(new MailMassage().setReceiverInfo(receiver));
    }

    /**
     * 发送普通邮件
     *
     * @param sender      发件人
     * @param password    通行证
     * @param receiver    收件人
     * @param mailTitle   主题
     * @param mailContext 正文
     * @return 验证码/fail 代表不同结果
     */

    public static String send(String sender, String password, String receiver, String mailTitle, String mailContext) {
        return send(
                new MailMassage()
                        .setSender(sender)
                        .setPassword(password)
                        .setReceiverInfo(receiver)
                        .setSubject(mailTitle)
                        .setContext(mailContext));
    }

    /**
     * 实际发送邮件的方法
     * 接收配置文件和邮件信息，直接发送邮件
     */
    private static String send(MailMassage mailMassage) {
        //根据发件人地址，选择不同的服务端
        makeProtocol(mailMassage.sender);

        //创建会话对象，用户邮件和服务器的交互
        Session session = Session.getDefaultInstance(MailUtil.props);
        //查看发送邮件的log,较为冗余，暂不使用
//        session.setDebug(true);
        //创建邮件
        MimeMessage message = new MimeMessage(session);
        //设置发件人，如果发件人名称为空，则使用发件地址
        //如果没有发件人，则为验证码邮件，使用默认发件人
        Transport transport;
        try {
            message.setFrom(new InternetAddress(
                    mailMassage.sender,
                    mailMassage.senderNikeName != null ? mailMassage.senderNikeName : mailMassage.sender,
                    "UTF-8"));

            //设置收件人，如果收件人名称为空，则使用收件地址
            message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(
                            mailMassage.receiver, mailMassage.receiverNikeName != null ? mailMassage.receiverNikeName : mailMassage.receiver,
                            "UTF-8"));
            //设置标题、正文
            message.setSubject(mailMassage.subject, "UTF-8");
            message.setContent(mailMassage.context, "text/html;charset=UTF-8");
            message.setSentDate(new Date());
            message.saveChanges();

            //使用session 获取传输对象
            transport = session.getTransport();
        } catch (Exception exception) {
            Logger.getGlobal().log(Level.WARNING, "Email information Settings are incorrect, please check");
            return "failed";
        }

        String hostTemp = "";
        //获取发件连接
        for (int i = 0; i < PORTS.length; i++) {
            try {
                //设置端口和ssl端口
                MailUtil.props.put("mail.smtp.port", PORTS[i]);
                transport.connect(mailMassage.sender, mailMassage.password);
                hostTemp = PORTS[i];
                break;
            } catch (Exception e) {
                //如果最后一次尝试仍然失败
                if (i == PORTS.length - 1) {
                    Logger.getGlobal().log(Level.WARNING, "no Matching Port Or Connection Exists");
                    return "failed";
                }
                //出现端口不匹配异常，使用日志提示，更换端口
                Logger.getGlobal().log(Level.WARNING, "CURRENT_PORT" + PORTS[i] + "connection Failed,Try A New Port" + PORTS[i + 1]);
            }
        }

        //发送
        try {
            transport.sendMessage(message, message.getAllRecipients());
            Logger.getGlobal().log(Level.INFO,
                    mailMassage.sender
                            + "\tTo\t" +
                            mailMassage.receiver
                            + "THE PORT FOR SENDING MAILS IS OCCUPIED PROCEDURE" + hostTemp);
            return mailMassage.mvcode;
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING,
                    mailMassage.sender
                            + "\tTo\t" +
                            mailMassage.receiver
                            + "FAILED TO SEND THE EMAIL PLEASE CHECK");
        } finally {
            //关闭传输对象
            try {
                transport.close();
            } catch (MessagingException e) {
                Logger.getGlobal().log(Level.WARNING, "THE TRANSPORT SHUTDOWN IS ABNORMAL PLEASE CHECK");
            }
        }

        return "failed";

    }


    //根据发件人地址，选择不同的服务端
    private static void makeProtocol(String sender) {
        //如果发件人是默认，即验证码邮件，不做修改
        if (sender.equals(DefaultSender)) {
            return;
        }
        //否则根据后缀选择smpt服务
        int first = sender.indexOf("@");
        int last = sender.lastIndexOf(".");
        String substring = sender.substring(first + 1, last);
        try {
            MailUtil.props.put("mail.smtp.host", Hosts.get(substring));
            //如果是qq邮箱，需要注掉SSL协议传输，使用非ssl的25端口,原因https://blog.csdn.net/qq_46864949/article/details/122851761?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-122851761-blog-124891609.pc_relevant_multi_platform_whitelistv1&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-122851761-blog-124891609.pc_relevant_multi_platform_whitelistv1&utm_relevant_index=1
            if (substring.equals("qq")) {
                MailUtil.props.remove("mail.smtp.socketFactory.class");
            }
        } catch (Exception e) {
            Logger.getGlobal().log(Level.WARNING, "NO MATCHING MAIL SERVER PLEASE CHECK");

        }

    }

    /**
     * @return 初始化props属性文件，加入通用的身份验证等配置
     */
    private static Properties createProperties() {
        Properties props = new Properties();

        //身份验证启用
        props.put("mail.smtp.auth", "true");
        //加密传输
        props.put("mail.smtp.starttls.enable", "true");
        //SSL协议传输，如果是QQ邮箱，需要注掉，原因https://blog.csdn.net/qq_46864949/article/details/122851761?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-122851761-blog-124891609.pc_relevant_multi_platform_whitelistv1&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7Edefault-1-122851761-blog-124891609.pc_relevant_multi_platform_whitelistv1&utm_relevant_index=1
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", false);
        //设置当前传输协议，比如smtp
        props.put("mail.transport.protocol", protocol);
        //设置默认传输客户端地址
        props.put("mail.smtp.host", DefaultHost);

        return props;
    }


    /**
     * @return 初始化hosts属性库
     */
    private static Map<String, String> createHosts() {
        Map<String, String> map = new HashMap<>();

        map.put("126", "smtp.126.com");
        map.put("163", "smtp.126.com");
        map.put("sina", "smtp.sina.com");
        map.put("gmail", "smtp.gmail.com");
        map.put("sohu", "smtp.sohu.com");
        map.put("qq", "smtp.qq.com");
        map.put("aliyun", "smtp.aliyun.com");


        return map;
    }


    /**
     * 邮件类，包含邮件信息
     */
    static class MailMassage {
        //发件人收件人
        public String sender = DefaultSender;//默认
        public String password = DefaultPassword;
        public String receiver;

        //发件人收件人名称，可选
        public String senderNikeName;//默认
        public String receiverNikeName;
        public String mvcode = vcode();//生成的验证码

        //主题，正文
        //默认正文标题为验证码
        public String subject = "IKUN";
        public String context = "【IKUN】欢迎使用验证码服务，您的验证码是" + mvcode + "请勿将验证码泄露于他人，有效期3分钟";

        public MailMassage setReceiverInfo(String receiver) {
            this.receiver = receiver;
            return this;
        }

        public MailMassage setSender(String sender) {
            this.sender = sender;
            return this;
        }

        public MailMassage setPassword(String password) {
            this.password = password;
            return this;
        }

        public MailMassage setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailMassage setContext(String context) {
            this.context = context;
            return this;
        }
    }

    //获取随机六位验证码
    private static String vcode() {
        //从性能、并发和资源三个角度来看，第一种方法使用StringBuilder的方式更好。
        /*
        IntStream.range(0, 6)
                .mapToObj(i -> (int) (Math.random() * 10))
                .map(String::valueOf)
                .collect(Collectors.joining());
         */
        //IntStream会有并发和性能影响

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int bi = (int) (Math.random() * 10);
            code.append(bi);
        }

        return code.toString();
    }

    public static String getDefaultSender() {
        return DefaultSender;
    }

    public static void setDefaultSender(String defaultSender) {
        DefaultSender = defaultSender;
    }

    public static String getDefaultPassword() {
        return DefaultPassword;
    }

    public static void setDefaultPassword(String defaultPassword) {
        DefaultPassword = defaultPassword;
    }
}

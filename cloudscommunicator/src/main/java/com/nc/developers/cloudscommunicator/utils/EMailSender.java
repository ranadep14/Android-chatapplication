package com.nc.developers.cloudscommunicator.utils;

import android.util.Log;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EMailSender{

    //host info.===>
    //gmail=>smtp.gmail.com
    //yahoo=>smtp.mail.yahoo.com

    private String hostName="",fromName="",toName="",subject="",messagee="";
    private Thread threadMailSender=null;
    private Runnable runnableMailSender=null;
    public EMailSender(String host, final String from, final String pass,
                       String to, String sub, String mess){
        hostName=fromName=subject=toName=messagee="";
        hostName=host;
        fromName=from;
        subject=sub;
        messagee=mess;
        toName=to;
        try{
            runnableMailSender=new Runnable() {
                @Override
                public void run() {
                    try{
                        Properties props = System.getProperties();
                        props.put("mail.smtp.auth","true");
                        props.put("mail.smtp.starttls.enable","true");
                        props.put("mail.smtp.host",hostName);
                        props.put("mail.smtp.port","465");// need to change port according to domain
                        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                        Authenticator auth = new Authenticator(){
                            protected PasswordAuthentication getPasswordAuthentication(){
                                return new PasswordAuthentication(from, pass);
                            }
                        };
                        Session session = Session.getInstance(props, auth);
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(fromName));
                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toName));
                        message.setSubject(subject);
                        message.setText(messagee);
                        Transport.send(message);
                    }catch(AddressException e){
                        Log.e("addressexception:",e.toString());
                    }catch(MessagingException e){
                        Log.e("messagingexception:",e.toString());
                    }catch(Exception e){
                        Log.e("expn_mail_send:",e.toString());
                    }
                }
            };
            if(threadMailSender==null){
                threadMailSender=new Thread(runnableMailSender);
                threadMailSender.start();
            }
        }catch(Exception e){
            Log.e("expn_send_mail:",e.toString());
        }finally{
            if(threadMailSender!=null){
                if(threadMailSender!=null && !threadMailSender.isInterrupted()){
                    threadMailSender.interrupt();
                    threadMailSender=null;
                }
            }
        }
    }
}
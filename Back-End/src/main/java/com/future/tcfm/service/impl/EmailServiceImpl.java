package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Notification;
import com.future.tcfm.model.NotificationEvent;
import com.future.tcfm.model.ReqResModel.EmailRequest;
import com.future.tcfm.model.User;
import com.future.tcfm.model.list.ExpenseIdContributed;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {


    public static final String EXPENSE_MESSAGE = " requested new expense ";
    public static final String EXPENSE_APPROVED_MESSAGE = " 's requested expense had been approved ";
    public static final String EXPENSE_REJECTED_MESSAGE = " 's requested expense had been rejected ";
    public static final String USER_LEFT_GROUP = " just left this group ";
    public static final String USER_JOINED_GROUP = " just joined this group ";
    public static final String PAYMENT_MESSAGE = " had made payment ";
    public static final String PAYMENT_APPROVED_MESSAGE = " 's payment had been approved by ";
    public static final String PAYMENT_REJECTED_MESSAGE = " 's payment had been rejected by ";
    private static final String PATH = "../assets/";

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    public JavaMailSender emailSender;
    private int yearNow= LocalDate.now().getYear();
    private int monthNow= LocalDate.now().getMonthValue();

    String monthNowStr= Month.of(monthNow).getDisplayName(TextStyle.FULL, Locale.ENGLISH);

    @Override
    public ResponseEntity simpleEmail(EmailRequest emailRequest) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getEmail());
            message.setSubject("Blibli Future Medan Batch - 3.0");
            message.setText("simple email sender!");

            this.emailSender.send(message);

         return new ResponseEntity<>("Email Sent!", HttpStatus.OK);
        }



        public void periodicMailSender( String email, Integer range) throws MessagingException {
        User user  = userRepository.findByEmail(email);
        String name = user.getName();
        String groupName = user.getGroupName();
        int monthBefore= LocalDate.now().getMonthValue()-range;
        if(monthBefore<1){{ monthBefore+=12; } }
        String monthBeforeStr= Month.of(monthBefore).getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setSubject("Team Cash Flow Management: Monthly Reminder Regular Payment");

        if (range==0) {
            helper.setText("<html><body>" +
                    "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fwww.blibli.com%2Fwcsstore%2FIndraprastha%2Fimages%2Fgdn%2Fimages%2Fhead-blibli.jpg&amp;t=1563260709&amp;ymreqid=f2fe503c-78f1-5207-1c71-ea000701a700&amp;sig=Yqh0WVnLasWdIARYJH9xBg--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                    "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu telah membayar iuran untuk bulan "+monthNowStr+" "+yearNow+"<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);
        }
        else if(range==1){
            helper.setText("<html><body>" +
                    "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fwww.blibli.com%2Fwcsstore%2FIndraprastha%2Fimages%2Fgdn%2Fimages%2Fhead-blibli.jpg&amp;t=1563260709&amp;ymreqid=f2fe503c-78f1-5207-1c71-ea000701a700&amp;sig=Yqh0WVnLasWdIARYJH9xBg--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                    "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu belum membayar iuran untuk bulan "+monthBeforeStr+" "+yearNow+"<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);
        }

        else {
            helper.setText("<html><body>" +
                    "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fwww.blibli.com%2Fwcsstore%2FIndraprastha%2Fimages%2Fgdn%2Fimages%2Fhead-blibli.jpg&amp;t=1563260709&amp;ymreqid=f2fe503c-78f1-5207-1c71-ea000701a700&amp;sig=Yqh0WVnLasWdIARYJH9xBg--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                    "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu belum membayar iuran untuk bulan "+monthBeforeStr+" "+yearNow+" - "+monthNowStr+" - "+yearNow+"<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);
        }
        this.emailSender.send(message);
    }

    @Override
    public void requestExpense(String email, String idExpense) throws MessagingException {
        User user  = userRepository.findByEmail(email);
        String name = user.getName();
        String groupName = user.getGroupName();
        Expense expense = expenseRepository.findByIdExpense(idExpense);
        User requester = userRepository.findByEmail(expense.getRequester());

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setSubject("Team Cash Flow Management: Monthly Reminder Regular Payment");

        helper.setText("<html><body>" +
                "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fwww.blibli.com%2Fwcsstore%2FIndraprastha%2Fimages%2Fgdn%2Fimages%2Fhead-blibli.jpg&amp;t=1563260709&amp;ymreqid=f2fe503c-78f1-5207-1c71-ea000701a700&amp;sig=Yqh0WVnLasWdIARYJH9xBg--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>"+requester.getName()+" Baru Saja Merequest Expense "+expense.getTitle()+"!<br>Detail : "+expense.getDetail()+"<br>Price : "+expense.getPrice()+"<br><br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);

        this.emailSender.send(message);
    }

    @Override
    public void userResign(String email) throws MessagingException {
        User user  = userRepository.findByEmail(email);
        String name = user.getName();
        String groupName = user.getGroupName();

        List<Expense> listExpense = new ArrayList<>();

        List<ExpenseIdContributed> expenseIdContributed = user.getExpenseIdContributed();
        for(ExpenseIdContributed expense: expenseIdContributed){
            Expense e = expenseRepository.findByIdExpense(expense.getIdExpense()) ;
            listExpense.add(e);
        }

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("mhabibofficial2@yahoo.com");
        helper.setSubject("Team Cash Flow Management: Monthly Reminder Regular Payment");

        helper.setText("<html><body>" +
                "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fwww.blibli.com%2Fwcsstore%2FIndraprastha%2Fimages%2Fgdn%2Fimages%2Fhead-blibli.jpg&amp;t=1563260709&amp;ymreqid=f2fe503c-78f1-5207-1c71-ea000701a700&amp;sig=Yqh0WVnLasWdIARYJH9xBg--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu Baru Saja Meninggalkan Group "+groupName+"<br><br>Berikut ini merupakan list penggunaan dana kamu<br><br>"+listExpense+"<br><br>Jumlah dana yang akan dikembalikan kepadamu ialah senilai : Rp. "+user.getBalance()+"<br>Harap Hubungi Admin Group.<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);

        this.emailSender.send(message);
    }

    @Override
    public ResponseEntity attachmentEmail(EmailRequest emailRequest) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, emailRequest.getMultipart());

        helper.setTo(emailRequest.getEmail());
        helper.setSubject("Blibli Future Medan Batch - 3.0");
        helper.setText("Hello, This is from attachment email sender!");
        FileSystemResource file = new FileSystemResource(new File(PATH+emailRequest.getFile()));

        helper.addAttachment("Pdf file", file);

        emailSender.send(message);

        return new ResponseEntity<>("Email Sent!", HttpStatus.OK);
    }
}

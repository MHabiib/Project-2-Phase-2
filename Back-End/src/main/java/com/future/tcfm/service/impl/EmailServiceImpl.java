package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.Group;
import com.future.tcfm.model.ReqResModel.EmailRequest;
import com.future.tcfm.model.User;
import com.future.tcfm.model.list.ExpenseContributedDetails;
import com.future.tcfm.model.list.ExpenseIdContributed;
import com.future.tcfm.repository.ExpenseRepository;
import com.future.tcfm.repository.GroupRepository;
import com.future.tcfm.repository.UserRepository;
import com.future.tcfm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String PATH = "../assets/";

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    public JavaMailSender emailSender;

    @Async
    public void periodicMailSender( String email, String monthBeforeStr,int yearBefore) throws MessagingException {
        User user  = userRepository.findByEmail(email);
        String name = user.getName();
        String groupName = user.getGroupName();
        String monthNowStr=Month.of(LocalDate.now().getMonthValue()).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int yearNow=LocalDate.now().getYear();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("mhabibofficial2@gmail.com");
        helper.setSubject("Team Cash Flow Management: Monthly Reminder Regular Payment");

        if (monthNowStr.equals(monthBeforeStr)) {
            helper.setText("<html><body>" +
                    "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                    "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu telah membayar iuran untuk bulan "+monthNowStr+" "+yearNow+"<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);
        }
        else {
            helper.setText("<html><body>" +
                    "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                    "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu belum membayar iuran untuk bulan "+monthBeforeStr+" "+yearBefore+" - "+monthNowStr+" - "+yearNow+"<br>Segera lakukan pembayaran anda.<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);
        }
        this.emailSender.send(message);
    }

    @Override
    public ResponseEntity simpleEmail(EmailRequest emailRequest) {
        return null;
    }

    @Async
    public void periodicMailReminderSender( String email) throws MessagingException {
        User user  = userRepository.findByEmail(email);
        String name = user.getName();
        String groupName = user.getGroupName();
        String monthNowStr=Month.of(LocalDate.now().getMonthValue()).getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int yearNow=LocalDate.now().getYear();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("mhabibofficial2@gmail.com");
        helper.setSubject("Team Cash Flow Management: Monthly Reminder Regular Payment");

        helper.setText("<html><body>" +
                "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Iuran bulanan group kamu akan dijalankan ke periode berikutnya, pada tanggal 10 "+monthNowStr+" "+yearNow+"<br>Pastikan anda telah membayar iuran bulanan anda.<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);

        this.emailSender.send(message);
    }

    @Async
    @Override
    public void emailNotification(String messages, String email) throws MessagingException {
        User user  = userRepository.findByEmail(email);
        String name = user.getName();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setSubject("Team Cash Flow Management: Notification");

        helper.setText("<html><body>" +
                "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>"+messages+"<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team - Blibli.com</p></td></tr></body></html>",true);

        this.emailSender.send(message);
    }

    @Async
    @Override
    public void monthlyCashStatement(String email) throws MessagingException {
        User user  = userRepository.findByEmail(email);
        String name = user.getName();
        String groupName = user.getGroupName();
        String expenseListStr="";
        List<ExpenseContributedDetails> listExpense = new ArrayList<>();
        Group group= groupRepository.findByName(user.getGroupName());
        List<Expense> expenseIdContributed = expenseRepository.findByGroupNameLikeAndGroupCurrentPeriodAtAndStatus(groupName,group.getCurrentPeriod(),true);
        if(expenseIdContributed!=null){
            for(Expense expense: expenseIdContributed){
                ExpenseContributedDetails expenseContributedDetails = new ExpenseContributedDetails();
                expenseContributedDetails.setTitle(expense.getTitle());
                expenseContributedDetails.setDetail(expense.getDetail());
                expenseContributedDetails.setPrice(expense.getPrice());
                listExpense.add(expenseContributedDetails);
                expenseListStr+=(expenseContributedDetails.toString());
            }
        }
        else{
            expenseListStr="\"Ooopss!!! Group anda belum ada kontribusi :C\"";
        }

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("mhabibofficial2@gmail.com");
        helper.setSubject("Team Cash Flow Management: Resignation");

        helper.setText("<html><body>" +
                "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu Baru Saja Meninggalkan Group "+groupName+"<br><br>Berikut ini merupakan list penggunaan dana kamu<br><br>"+expenseListStr+"<br><br>Jumlah dana yang akan dikembalikan kepadamu ialah senilai : Rp. "+user.getBalance()+"<br>Harap Hubungi Admin Group Untuk Prosedur Pengambilan Uang Kembali.<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);

        this.emailSender.send(message);
    }

    @Async
    @Override
    public ResponseEntity userResign(String email) throws MessagingException {
        User user  = userRepository.findByEmail(email);
        String name = user.getName();
        String groupName = user.getGroupName();
        String expenseListStr="";
        List<ExpenseContributedDetails> listExpense = new ArrayList<>();

        List<ExpenseIdContributed> expenseIdContributed = user.getExpenseIdContributed();
        if(expenseIdContributed!=null){
            for(ExpenseIdContributed expense: expenseIdContributed){
                Expense e = expenseRepository.findByIdExpense(expense.getIdExpense()) ;
                ExpenseContributedDetails expenseContributedDetails = new ExpenseContributedDetails();
                expenseContributedDetails.setTitle(e.getTitle());
                expenseContributedDetails.setDetail(e.getDetail());
                expenseContributedDetails.setPrice(expenseIdContributed.get(0).getUsedBalance());//RECHECK!!!!!!!!!!!
                listExpense.add(expenseContributedDetails);
                expenseListStr+=(expenseContributedDetails.toString());
            }
        }
        else{
            expenseListStr="\"Ooopss!!! anda belum ada kontribusi dalam group ini :C\"";
        }

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("mhabibofficial2@gmail.com");
        helper.setSubject("Team Cash Flow Management: Resignation");

        helper.setText("<html><body>" +
                "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu Baru Saja Meninggalkan Group "+groupName+"<br><br>Berikut ini merupakan list penggunaan dana kamu<br><br>"+expenseListStr+"<br><br>Jumlah dana yang akan dikembalikan kepadamu ialah senilai : Rp. "+user.getBalance()+"<br>Harap Hubungi Admin Group Untuk Prosedur Pengambilan Uang Kembali.<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);

        this.emailSender.send(message);
        return null;
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

/*    @Override
    public ResponseEntity simpleEmail(EmailRequest emailRequest) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getEmail());
            message.setSubject("Blibli Future Medan Batch - 3.0");
            message.setText("simple email sender!");

            this.emailSender.send(message);

         return new ResponseEntity<>("Email Sent!", HttpStatus.OK);
}*/

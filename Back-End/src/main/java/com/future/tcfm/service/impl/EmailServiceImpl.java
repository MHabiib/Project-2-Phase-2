package com.future.tcfm.service.impl;

import com.future.tcfm.model.Expense;
import com.future.tcfm.model.ReqResModel.EmailRequest;
import com.future.tcfm.model.User;
import com.future.tcfm.model.list.ExpenseContributedDetails;
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
import org.springframework.mail.javamail.MimeMessageHelper;
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
                    "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                    "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu telah membayar iuran untuk bulan "+monthNowStr+" "+yearNow+"<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);
        }
        else if(range==1){
            helper.setText("<html><body>" +
                    "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                    "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu belum membayar iuran untuk bulan "+monthBeforeStr+" "+yearNow+"<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);
        }

        else {
            helper.setText("<html><body>" +
                    "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
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
                "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>"+requester.getName()+" Baru Saja Merequest Expense "+expense.getTitle()+"!<br>Detail : "+expense.getDetail()+"<br>Price : "+expense.getPrice()+"<br><br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);

        this.emailSender.send(message);
    }

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

        //HOW TO SHOW ARRY ON HTML???????????????????????

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("mhabibofficial2@gmail.com");
        helper.setSubject("Team Cash Flow Management: Resignation");

        helper.setText("<html><body>" +
                "<img src=\"https://ecp.yusercontent.com/mail?url=https%3A%2F%2Fattachment.freshdesk.com%2Finline%2Fattachment%3Ftoken%3DeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MzUwMTYyOTE1ODgsImRvbWFpbiI6ImJsaWJsaWNhcmUuZnJlc2hkZXNrLmNvbSIsImFjY291bnRfaWQiOjc4OTM5M30.cHSBN2d9_8FZrmY3y6-n5b5FY3RUzJ-4JV6SD_EWXfc&t=1563855732&ymreqid=f2fe503c-78f1-5207-1c52-e00005011400&sig=kAn2UYZJzmVcvzCbWALl_g--~C\" alt=\"www.blibli.com\" width=\"700\" height=\"100\" style=\"border:0px;\">" +
                "<tr><td style=\"padding:15px;\"><p>Halo "+name+"<br><br>Kamu Baru Saja Meninggalkan Group "+groupName+"<br><br>Berikut ini merupakan list penggunaan dana kamu<br><br>"+expenseListStr+"<br><br>Jumlah dana yang akan dikembalikan kepadamu ialah senilai : Rp. "+user.getBalance()+"<br>Harap Hubungi Admin Group.<br><br>Semoga hari anda menyenangkan. Terima Kasih.<br><br><br><br>Salam hangat,<br>Admin Team "+groupName+" - Blibli.com</p></td></tr></body></html>",true);

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

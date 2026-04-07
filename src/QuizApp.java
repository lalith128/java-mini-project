import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
public class QuizApp extends JFrame{
    private final CardLayout cards=new CardLayout();
    private final JPanel mainpanel=new JPanel(cards);
    private static final Color BG=new Color(240,240,240),PRIMARY=new Color(51,102,153),
            DARK=new Color(33,33,33),GREEN=new Color(40,140,60),RED=new Color(180,40,40);
    private static final Font TITLE=new Font("Segoe UI",Font.BOLD,22),
            HEADER=new Font("Segoe UI",Font.BOLD,16),BODY=new Font("Segoe UI",Font.PLAIN,14),
            SMALL=new Font("Segoe UI",Font.PLAIN,12),MONO=new Font("Consolas",Font.BOLD,18);
    public QuizApp(){
        setTitle("Online Quiz & Assessment System");
        setSize(900,650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800,550));
        mainpanel.add(buildhome(),"home");
        mainpanel.add(buildadmin(),"admin");
        add(mainpanel);
        cards.show(mainpanel,"home");
    }
    private JButton uibtn(String text,Color bg){
        JButton b=new JButton(text);
        b.setFont(BODY); b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createEmptyBorder(10,25,10,25));
        b.setOpaque(true); b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){b.setBackground(bg.darker());}
            public void mouseExited(MouseEvent e){b.setBackground(bg);}
        });
        return b;
    }
    private JLabel uilabel(String text,Font f,Color c,int align){
        JLabel l=new JLabel(text,align); l.setFont(f); l.setForeground(c); return l;
    }
    private JPanel uipanel(Color bg,LayoutManager l){
        JPanel p=new JPanel(l); p.setBackground(bg); return p;
    }
    private JPanel createheader(String title,Color bg){
        JPanel h=uipanel(bg,new BorderLayout());
        h.setPreferredSize(new Dimension(0,60));
        h.add(uilabel("  "+title,HEADER,Color.WHITE,JLabel.LEFT),BorderLayout.CENTER);
        return h;
    }
    private JPanel buildhome(){
        JPanel p=uipanel(BG,new BorderLayout());
        p.add(createheader("Online Quiz & Assessment System",PRIMARY),BorderLayout.NORTH);
        JPanel center=uipanel(BG,new GridBagLayout());
        GridBagConstraints g=new GridBagConstraints();
        g.insets=new Insets(10,10,10,10); g.fill=GridBagConstraints.HORIZONTAL;
        g.gridx=0; g.gridy=0; g.gridwidth=2;
        center.add(uilabel("Welcome! Test Your Java Knowledge",HEADER,DARK,JLabel.CENTER),g);
        g.gridy=1;
        center.add(uilabel("Choose an option below to get started",BODY,Color.GRAY,JLabel.CENTER),g);
        JTextField namefield=new JTextField(20); namefield.setFont(BODY);
        JSpinner countspin=new JSpinner(new SpinnerNumberModel(10,5,50,5));
        JSpinner timespin=new JSpinner(new SpinnerNumberModel(30,10,120,5));
        Component[] inputs={namefield,countspin,timespin};
        String[] labels={"Your Name:","No. of Questions:","Seconds per Question:"};
        for(int i=0;i<3;i++){
            g.gridy=2+i; g.gridwidth=1;
            center.add(uilabel(labels[i],BODY,DARK,JLabel.LEFT),g);
            g.gridx=1; center.add(inputs[i],g); g.gridx=0;
        }
        g.gridy=5; g.gridwidth=2; g.insets=new Insets(20,10,5,10);
        JButton startbtn=uibtn("Start Quiz",PRIMARY);
        center.add(startbtn,g);
        g.gridy=6; g.insets=new Insets(5,10,5,10);
        JButton histbtn=uibtn("View Past Results",DARK);
        center.add(histbtn,g);
        g.gridy=7;
        JButton adminbtn=uibtn("Admin Panel",new Color(100,100,100));
        center.add(adminbtn,g);
        p.add(center,BorderLayout.CENTER);
        JLabel footer=uilabel("  Java Mini Project | 2026",SMALL,Color.GRAY,JLabel.LEFT);
        footer.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        p.add(footer,BorderLayout.SOUTH);
        startbtn.addActionListener(e->{
            String name=namefield.getText().trim();
            if(name.isEmpty()){JOptionPane.showMessageDialog(this,"Please enter your name!","Error",JOptionPane.ERROR_MESSAGE); return;}
            startquiz(name,(int)countspin.getValue(),(int)timespin.getValue());
        });
        histbtn.addActionListener(e->showhistory());
        adminbtn.addActionListener(e->{
            String pwd=JOptionPane.showInputDialog(this,"Enter Admin Password:","Admin Login",JOptionPane.PLAIN_MESSAGE);
            if("admin".equals(pwd)) cards.show(mainpanel,"admin");
            else if(pwd!=null) JOptionPane.showMessageDialog(this,"Wrong password!","Error",JOptionPane.ERROR_MESSAGE);
        });
        return p;
    }
    private void startquiz(String name,int count,int timeperq){
        java.util.List<Question> all=DataStore.loadquestions();
        if(all.size()<count) count=all.size();
        Collections.shuffle(all);
        java.util.List<Question> quiz=new ArrayList<>(all.subList(0,count));
        int[] ans=new int[count]; Arrays.fill(ans,-1);
        JPanel p=uipanel(BG,new BorderLayout());
        JLabel qnum=uilabel("  Question 1 of "+count,HEADER,Color.WHITE,JLabel.LEFT);
        JLabel timerlbl=uilabel("Time: "+timeperq+"s   ",MONO,Color.YELLOW,JLabel.RIGHT);
        JProgressBar progress=new JProgressBar(0,count);
        progress.setValue(0); progress.setStringPainted(true); progress.setFont(SMALL);
        progress.setForeground(PRIMARY);
        JPanel topwrap=uipanel(PRIMARY,new BorderLayout());
        topwrap.add(qnum,BorderLayout.WEST); topwrap.add(timerlbl,BorderLayout.EAST);
        JPanel topall=uipanel(BG,new BorderLayout());
        topall.add(topwrap,BorderLayout.NORTH); topall.add(progress,BorderLayout.SOUTH);
        p.add(topall,BorderLayout.NORTH);
        JPanel qpanel=new JPanel();
        qpanel.setLayout(new BoxLayout(qpanel,BoxLayout.Y_AXIS));
        qpanel.setBackground(Color.WHITE);
        qpanel.setBorder(BorderFactory.createEmptyBorder(20,30,20,30));
        JLabel qtext=uilabel("",new Font("Segoe UI",Font.BOLD,16),DARK,JLabel.LEFT);
        qtext.setAlignmentX(Component.LEFT_ALIGNMENT);
        qpanel.add(qtext); qpanel.add(Box.createVerticalStrut(15));
        ButtonGroup bg=new ButtonGroup();
        JRadioButton[] opts=new JRadioButton[4];
        for(int i=0;i<4;i++){
            opts[i]=new JRadioButton(); opts[i].setFont(BODY); opts[i].setBackground(Color.WHITE);
            opts[i].setAlignmentX(Component.LEFT_ALIGNMENT); opts[i].setBorder(BorderFactory.createEmptyBorder(5,10,5,0));
            bg.add(opts[i]); qpanel.add(opts[i]); qpanel.add(Box.createVerticalStrut(5));
        }
        JLabel metalbl=uilabel("",SMALL,Color.GRAY,JLabel.LEFT);
        metalbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        qpanel.add(Box.createVerticalStrut(10)); qpanel.add(metalbl);
        p.add(new JScrollPane(qpanel),BorderLayout.CENTER);
        JPanel btnpanel=uipanel(BG,new FlowLayout(FlowLayout.CENTER,15,10));
        JButton prevbtn=uibtn("Previous",new Color(80,80,80)),nextbtn=uibtn("Next",PRIMARY),
                submitbtn=uibtn("Submit Quiz",GREEN);
        submitbtn.setVisible(false);
        btnpanel.add(prevbtn); btnpanel.add(nextbtn); btnpanel.add(submitbtn);
        p.add(btnpanel,BorderLayout.SOUTH);
        final int total=count;
        int[] cur={0};
        int[] timeleft={timeperq};
        long starttime=System.currentTimeMillis();
        javax.swing.Timer timer=new javax.swing.Timer(1000,e->{
            timeleft[0]--;
            timerlbl.setText("Time: "+timeleft[0]+"s   ");
            timerlbl.setForeground(timeleft[0]<=5?Color.RED:Color.YELLOW);
            if(timeleft[0]<=0){
                saveanswer(opts,ans,cur[0]);
                if(cur[0]<total-1){cur[0]++; loadq(quiz,cur[0],total,qtext,opts,bg,qnum,metalbl,progress,ans,prevbtn,nextbtn,submitbtn); timeleft[0]=timeperq;}
                else{((javax.swing.Timer)e.getSource()).stop(); finishquiz(name,quiz,ans,starttime);}
            }
        });
        loadq(quiz,0,total,qtext,opts,bg,qnum,metalbl,progress,ans,prevbtn,nextbtn,submitbtn);
        timer.start();
        nextbtn.addActionListener(e->{saveanswer(opts,ans,cur[0]); if(cur[0]<total-1){cur[0]++; loadq(quiz,cur[0],total,qtext,opts,bg,qnum,metalbl,progress,ans,prevbtn,nextbtn,submitbtn); timeleft[0]=timeperq;}});
        prevbtn.addActionListener(e->{saveanswer(opts,ans,cur[0]); if(cur[0]>0){cur[0]--; loadq(quiz,cur[0],total,qtext,opts,bg,qnum,metalbl,progress,ans,prevbtn,nextbtn,submitbtn); timeleft[0]=timeperq;}});
        submitbtn.addActionListener(e->{
            saveanswer(opts,ans,cur[0]);
            int unanswered=0; for(int a:ans) if(a==-1) unanswered++;
            if(JOptionPane.showConfirmDialog(this,unanswered>0?unanswered+" question(s) unanswered. Submit anyway?":"Are you sure?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                timer.stop(); finishquiz(name,quiz,ans,starttime);
            }
        });
        mainpanel.add(p,"quiz"); cards.show(mainpanel,"quiz");
    }
    private void saveanswer(JRadioButton[] opts,int[] ans,int idx){
        for(int i=0;i<4;i++) if(opts[i].isSelected()){ans[idx]=i; break;}
    }
    private void finishquiz(String name,java.util.List<Question> quiz,int[] ans,long starttime){
        QuizResult res=new QuizResult(name,quiz,ans,(System.currentTimeMillis()-starttime)/1000);
        DataStore.saveresult(res);
        showresult(res);
    }
    private void loadq(java.util.List<Question> quiz,int idx,int total,JLabel qtext,JRadioButton[] opts,ButtonGroup bg,
                       JLabel qnum,JLabel metalbl,JProgressBar progress,int[] ans,JButton prevbtn,JButton nextbtn,JButton submitbtn){
        Question q=quiz.get(idx);
        qnum.setText("  Question "+(idx+1)+" of "+total);
        qtext.setText("<html>Q"+(idx+1)+". "+q.gettext()+"</html>");
        metalbl.setText("Category: "+q.getcategory()+"  |  Difficulty: "+q.getdifflabel());
        progress.setValue(idx+1); progress.setString((idx+1)+" / "+total);
        bg.clearSelection();
        String[] options=q.getoptions();
        for(int i=0;i<4;i++){
            opts[i].setText((char)('A'+i)+")   "+options[i]);
            if(ans[idx]==i) opts[i].setSelected(true);
        }
        prevbtn.setEnabled(idx>0);
        nextbtn.setVisible(idx<total-1);
        submitbtn.setVisible(idx==total-1);
    }
    private void showresult(QuizResult res){
        JPanel p=uipanel(BG,new BorderLayout());
        boolean passed=res.getpercent()>=50;
        JPanel header=uipanel(passed?GREEN:RED,new GridLayout(2,1));
        header.setPreferredSize(new Dimension(0,80));
        header.add(uilabel("Quiz Complete! "+res.getverdict(),TITLE,Color.WHITE,JLabel.CENTER));
        header.add(uilabel("Score: "+res.getscore()+"/"+res.gettotal()+"  |  "+String.format("%.1f%%",res.getpercent())+"  |  Grade: "+res.getgrade()+"  |  Time: "+res.gettimestr(),BODY,Color.WHITE,JLabel.CENTER));
        p.add(header,BorderLayout.NORTH);
        JTabbedPane tabs=new JTabbedPane(); tabs.setFont(BODY);
        JPanel reviewpanel=new JPanel();
        reviewpanel.setLayout(new BoxLayout(reviewpanel,BoxLayout.Y_AXIS));
        reviewpanel.setBackground(Color.WHITE);
        for(int i=0;i<res.getquestions().size();i++){
            Question q=res.getquestions().get(i);
            JPanel card=uipanel(res.getcorrect()[i]?new Color(230,255,230):new Color(255,230,230),new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.LIGHT_GRAY),BorderFactory.createEmptyBorder(10,15,10,15)));
            String icon=res.getcorrect()[i]?"CORRECT":"WRONG";
            String yourans=res.getanswers()[i]>=0?(char)('A'+res.getanswers()[i])+")  "+q.getoptions()[res.getanswers()[i]]:"Not Answered";
            String rightans=(char)('A'+q.getcorrect())+")  "+q.getoptions()[q.getcorrect()];
            card.add(uilabel("<html>Q"+(i+1)+". "+q.gettext()+"<br>["+icon+"]<br>Your Answer: "+yourans+"<br>Correct Answer: "+rightans+"</html>",BODY,DARK,JLabel.LEFT));
            reviewpanel.add(card);
        }
        JScrollPane reviewscroll=new JScrollPane(reviewpanel);
        reviewscroll.getVerticalScrollBar().setUnitIncrement(16);
        tabs.addTab("Question Review",reviewscroll);
        JPanel perfpanel=new JPanel();
        perfpanel.setLayout(new BoxLayout(perfpanel,BoxLayout.Y_AXIS));
        perfpanel.setBackground(Color.WHITE);
        perfpanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        JLabel catlbl=uilabel("Category-wise Performance:",HEADER,DARK,JLabel.LEFT);
        catlbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        perfpanel.add(catlbl);
        perfpanel.add(Box.createVerticalStrut(8));
        String[] catcols={"Category","Total","Correct","Accuracy"};
        Map<String,int[]> catstats=res.getcategorystats();
        Object[][] catdata=new Object[catstats.size()][4];
        int row=0;
        for(Map.Entry<String,int[]> e:catstats.entrySet()){
            catdata[row][0]=e.getKey(); catdata[row][1]=e.getValue()[0];
            catdata[row][2]=e.getValue()[1]; catdata[row][3]=String.format("%.0f%%",e.getValue()[1]*100.0/e.getValue()[0]);
            row++;
        }
        JTable cattable=new JTable(catdata,catcols);
        cattable.setFont(BODY); cattable.setRowHeight(28); cattable.getTableHeader().setFont(HEADER); cattable.setEnabled(false);
        JScrollPane catscroll=new JScrollPane(cattable);
        catscroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        catscroll.setPreferredSize(new Dimension(0,Math.min(200,28*catstats.size()+32)));
        catscroll.setMaximumSize(new Dimension(Integer.MAX_VALUE,Math.min(200,28*catstats.size()+32)));
        perfpanel.add(catscroll);
        perfpanel.add(Box.createVerticalStrut(20));
        JLabel difflbl=uilabel("Difficulty-wise Performance:",HEADER,DARK,JLabel.LEFT);
        difflbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        perfpanel.add(difflbl);
        perfpanel.add(Box.createVerticalStrut(8));
        String[] diffcols={"Difficulty","Total","Correct","Accuracy"};
        Map<String,int[]> diffstats=res.getdiffstats();
        Object[][] diffdata=new Object[diffstats.size()][4];
        row=0;
        for(Map.Entry<String,int[]> e:diffstats.entrySet()){
            diffdata[row][0]=e.getKey(); diffdata[row][1]=e.getValue()[0];
            diffdata[row][2]=e.getValue()[1]; diffdata[row][3]=String.format("%.0f%%",e.getValue()[1]*100.0/e.getValue()[0]);
            row++;
        }
        JTable difftable=new JTable(diffdata,diffcols);
        difftable.setFont(BODY); difftable.setRowHeight(28); difftable.getTableHeader().setFont(HEADER); difftable.setEnabled(false);
        JScrollPane diffscroll=new JScrollPane(difftable);
        diffscroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        diffscroll.setPreferredSize(new Dimension(0,Math.min(200,28*diffstats.size()+32)));
        diffscroll.setMaximumSize(new Dimension(Integer.MAX_VALUE,Math.min(200,28*diffstats.size()+32)));
        perfpanel.add(diffscroll);
        perfpanel.add(Box.createVerticalGlue());
        JScrollPane perfscroll=new JScrollPane(perfpanel);
        perfscroll.getVerticalScrollBar().setUnitIncrement(16);
        tabs.addTab("Performance Analysis",perfscroll);
        p.add(tabs,BorderLayout.CENTER);
        JPanel btnpanel=uipanel(BG,new FlowLayout(FlowLayout.CENTER,15,10));
        JButton homebtn=uibtn("Back to Home",PRIMARY),retrybtn=uibtn("Retry Quiz",GREEN);
        homebtn.addActionListener(e->cards.show(mainpanel,"home"));
        retrybtn.addActionListener(e->cards.show(mainpanel,"home"));
        btnpanel.add(homebtn); btnpanel.add(retrybtn);
        p.add(btnpanel,BorderLayout.SOUTH);
        mainpanel.add(p,"result"); cards.show(mainpanel,"result");
    }
    private void showhistory(){
        java.util.List<QuizResult> results=DataStore.loadresults();
        JPanel p=uipanel(BG,new BorderLayout());
        JPanel header=uipanel(DARK,new BorderLayout()); header.setPreferredSize(new Dimension(0,50));
        header.add(uilabel("  Past Quiz Results",HEADER,Color.WHITE,JLabel.LEFT),BorderLayout.WEST);
        JButton backbtn=uibtn("Back",PRIMARY);
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.RIGHT)); bp.setOpaque(false); bp.add(backbtn);
        header.add(bp,BorderLayout.EAST);
        p.add(header,BorderLayout.NORTH);
        if(results.isEmpty()) p.add(uilabel("No results yet. Take a quiz first!",HEADER,Color.GRAY,JLabel.CENTER),BorderLayout.CENTER);
        else{
            String[] cols={"#","Name","Score","Percentage","Grade","Time","Date"};
            Object[][] data=new Object[results.size()][7];
            for(int i=0;i<results.size();i++){
                QuizResult r=results.get(i);
                data[i]=new Object[]{i+1,r.getuser(),r.getscore()+"/"+r.gettotal(),String.format("%.1f%%",r.getpercent()),r.getgrade(),r.gettimestr(),r.getdatestr()};
            }
            JTable table=new JTable(data,cols);
            table.setFont(BODY); table.setRowHeight(30); table.getTableHeader().setFont(HEADER);
            table.setAutoCreateRowSorter(true);
            p.add(new JScrollPane(table),BorderLayout.CENTER);
        }
        backbtn.addActionListener(e->cards.show(mainpanel,"home"));
        mainpanel.add(p,"history"); cards.show(mainpanel,"history");
    }
    private JPanel buildadmin(){
        JPanel p=uipanel(BG,new BorderLayout());
        JPanel header=uipanel(new Color(100,100,100),new BorderLayout()); header.setPreferredSize(new Dimension(0,50));
        header.add(uilabel("  Admin Panel - Manage Questions",HEADER,Color.WHITE,JLabel.LEFT),BorderLayout.WEST);
        JButton backbtn=uibtn("Logout",RED);
        JPanel bp=new JPanel(new FlowLayout(FlowLayout.RIGHT)); bp.setOpaque(false); bp.add(backbtn);
        header.add(bp,BorderLayout.EAST);
        p.add(header,BorderLayout.NORTH);
        DefaultListModel<String> listmodel=new DefaultListModel<>();
        JList<String> qlist=new JList<>(listmodel);
        qlist.setFont(BODY); qlist.setFixedCellHeight(30);
        Runnable refresh=()->{
            listmodel.clear();
            java.util.List<Question> qs=DataStore.loadquestions();
            for(int i=0;i<qs.size();i++) listmodel.addElement((i+1)+". ["+qs.get(i).getdifflabel()+"]  "+qs.get(i).gettext());
        };
        refresh.run();
        p.add(new JScrollPane(qlist),BorderLayout.CENTER);
        JPanel btnpanel=uipanel(BG,new FlowLayout(FlowLayout.CENTER,10,10));
        JButton addbtn=uibtn("Add Question",GREEN),editbtn=uibtn("Edit Selected",PRIMARY),
                delbtn=uibtn("Delete Selected",RED),viewbtn=uibtn("View Details",DARK);
        btnpanel.add(addbtn); btnpanel.add(editbtn); btnpanel.add(delbtn); btnpanel.add(viewbtn);
        p.add(btnpanel,BorderLayout.SOUTH);
        backbtn.addActionListener(e->cards.show(mainpanel,"home"));
        addbtn.addActionListener(e->{Question q=showqdialog(null); if(q!=null){java.util.List<Question> qs=DataStore.loadquestions(); qs.add(q); DataStore.savequestions(qs); refresh.run();}});
        editbtn.addActionListener(e->{int idx=qlist.getSelectedIndex(); if(idx<0){JOptionPane.showMessageDialog(this,"Select a question first!"); return;} java.util.List<Question> qs=DataStore.loadquestions(); Question edited=showqdialog(qs.get(idx)); if(edited!=null){qs.set(idx,edited); DataStore.savequestions(qs); refresh.run();}});
        delbtn.addActionListener(e->{int idx=qlist.getSelectedIndex(); if(idx<0){JOptionPane.showMessageDialog(this,"Select a question first!"); return;} if(JOptionPane.showConfirmDialog(this,"Delete this question?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){java.util.List<Question> qs=DataStore.loadquestions(); qs.remove(idx); DataStore.savequestions(qs); refresh.run();}});
        viewbtn.addActionListener(e->{int idx=qlist.getSelectedIndex(); if(idx<0){JOptionPane.showMessageDialog(this,"Select a question first!"); return;} JOptionPane.showMessageDialog(this,DataStore.loadquestions().get(idx).toString(),"Question Details",JOptionPane.INFORMATION_MESSAGE);});
        return p;
    }
    private Question showqdialog(Question existing){
        JTextField txt=new JTextField(existing!=null?existing.gettext():"",30);
        JTextField[] opts=new JTextField[4];
        for(int i=0;i<4;i++) opts[i]=new JTextField(existing!=null?existing.getoptions()[i]:"",25);
        JComboBox<String> correctbox=new JComboBox<>(new String[]{"A","B","C","D"});
        if(existing!=null) correctbox.setSelectedIndex(existing.getcorrect());
        JTextField catfield=new JTextField(existing!=null?existing.getcategory():"General",15);
        JComboBox<String> diffbox=new JComboBox<>(new String[]{"Easy","Medium","Hard"});
        if(existing!=null) diffbox.setSelectedIndex(existing.getdifficulty()-1);
        JPanel form=new JPanel(new GridLayout(0,2,5,5));
        String[] labels={"Question:","Option A:","Option B:","Option C:","Option D:","Correct Answer:","Category:","Difficulty:"};
        Component[] fields={txt,opts[0],opts[1],opts[2],opts[3],correctbox,catfield,diffbox};
        for(int i=0;i<labels.length;i++){form.add(new JLabel(labels[i])); form.add(fields[i]);}
        if(JOptionPane.showConfirmDialog(this,form,existing!=null?"Edit Question":"Add Question",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION){
            String text=txt.getText().trim();
            if(text.isEmpty()){JOptionPane.showMessageDialog(this,"Question cannot be empty!"); return null;}
            String[] options=Arrays.stream(opts).map(f->f.getText().trim()).toArray(String[]::new);
            if(Arrays.stream(options).anyMatch(String::isEmpty)){JOptionPane.showMessageDialog(this,"All options must be filled!"); return null;}
            return new Question(text,options,correctbox.getSelectedIndex(),catfield.getText().trim(),diffbox.getSelectedIndex()+1);
        }
        return null;
    }
    public static void main(String[] args){
        try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception e){}
        SwingUtilities.invokeLater(()->new QuizApp().setVisible(true));
    }
}

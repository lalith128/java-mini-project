import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class QuizResult implements Serializable{
    private String user;
    private int total;
    private int score;
    private int[] answers;
    private boolean[] correct;
    private long timetaken;
    private Date date;
    private List<Question> questions;
    public QuizResult(String user,List<Question> questions,int[] answers,long timetaken){
        this.user=user;
        this.questions=questions;
        this.answers=answers;
        this.timetaken=timetaken;
        this.date=new Date();
        this.total=questions.size();
        this.correct=new boolean[total];
        this.score=0;
        for(int i=0;i<total;i++){
            correct[i]=questions.get(i).isright(answers[i]);
            if(correct[i]) score++;
        }
    }
    public String getuser(){return user;}
    public int gettotal(){return total;}
    public int getscore(){return score;}
    public int[] getanswers(){return answers;}
    public boolean[] getcorrect(){return correct;}
    public long gettimetaken(){return timetaken;}
    public Date getdate(){return date;}
    public List<Question> getquestions(){return questions;}
    public double getpercent(){
        return (score*100.0)/total;
    }
    public String getgrade(){
        double p=getpercent();
        if(p>=90) return "A+";
        if(p>=80) return "A";
        if(p>=70) return "B";
        if(p>=60) return "C";
        if(p>=50) return "D";
        return "F";
    }
    public String getverdict(){
        double p=getpercent();
        if(p>=90) return "Outstanding!";
        if(p>=80) return "Excellent!";
        if(p>=70) return "Good Job!";
        if(p>=60) return "Not Bad.";
        if(p>=50) return "Needs Improvement.";
        return "Better Luck Next Time.";
    }
    public Map<String,int[]> getcategorystats(){
        Map<String,int[]> map=new HashMap<>();
        for(int i=0;i<total;i++){
            String cat=questions.get(i).getcategory();
            int[] arr=map.getOrDefault(cat,new int[]{0,0});
            arr[0]++;
            if(correct[i]) arr[1]++;
            map.put(cat,arr);
        }
        return map;
    }
    public Map<String,int[]> getdiffstats(){
        Map<String,int[]> map=new HashMap<>();
        for(int i=0;i<total;i++){
            String diff=questions.get(i).getdifflabel();
            int[] arr=map.getOrDefault(diff,new int[]{0,0});
            arr[0]++;
            if(correct[i]) arr[1]++;
            map.put(diff,arr);
        }
        return map;
    }
    public String getdatestr(){
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm").format(date);
    }
    public String gettimestr(){
        long min=timetaken/60;
        long sec=timetaken%60;
        return min+"m "+sec+"s";
    }
}

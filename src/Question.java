import java.io.Serializable;
public class Question implements Serializable{
    private String text;
    private String[] options;
    private int correct;
    private String category;
    private int difficulty;
    public Question(String text,String[] options,int correct,String category,int difficulty){
        this.text=text;
        this.options=options;
        this.correct=correct;
        this.category=category;
        this.difficulty=difficulty;
    }
    public String gettext(){return text;}
    public String[] getoptions(){return options;}
    public int getcorrect(){return correct;}
    public String getcategory(){return category;}
    public int getdifficulty(){return difficulty;}
    public boolean isright(int ans){return ans==correct;}
    public String getdifflabel(){
        switch(difficulty){
            case 1: return "Easy";
            case 2: return "Medium";
            case 3: return "Hard";
            default: return "Unknown";
        }
    }
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append(text).append("\n");
        for(int i=0;i<options.length;i++){
            sb.append("  ").append((char)('A'+i)).append(") ").append(options[i]).append("\n");
        }
        sb.append("Answer: ").append((char)('A'+correct));
        sb.append(" | Category: ").append(category);
        sb.append(" | Difficulty: ").append(getdifflabel());
        return sb.toString();
    }
}

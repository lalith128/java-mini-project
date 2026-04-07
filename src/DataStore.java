import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class DataStore{
    private static final String QFILE="questions.dat";
    private static final String RFILE="results.dat";
    @SuppressWarnings("unchecked")
    public static List<Question> loadquestions(){
        File f=new File(QFILE);
        if(!f.exists()) return getdefaults();
        try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(f))){
            return (List<Question>)in.readObject();
        }catch(Exception e){
            System.out.println("Error loading questions: "+e.getMessage());
            return getdefaults();
        }
    }
    public static void savequestions(List<Question> list){
        try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(QFILE))){
            out.writeObject(list);
        }catch(Exception e){
            System.out.println("Error saving questions: "+e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    public static List<QuizResult> loadresults(){
        File f=new File(RFILE);
        if(!f.exists()) return new ArrayList<>();
        try(ObjectInputStream in=new ObjectInputStream(new FileInputStream(f))){
            return (List<QuizResult>)in.readObject();
        }catch(Exception e){
            System.out.println("Error loading results: "+e.getMessage());
            return new ArrayList<>();
        }
    }
    public static void saveresult(QuizResult r){
        List<QuizResult> list=loadresults();
        list.add(r);
        try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream(RFILE))){
            out.writeObject(list);
        }catch(Exception e){
            System.out.println("Error saving result: "+e.getMessage());
        }
    }
    private static List<Question> getdefaults(){
        List<Question> list=new ArrayList<>();
        //neural network questions
        list.add(new Question("Which of the following is not a type of layer in a neural network?",
                new String[]{"Input","Hidden","Output","Recursive"},3,"Neural Networks",1));

        list.add(new Question("What is a perceptron?",
                new String[]{"Type of loss function","Type of layer","Basic unit of a neural network","Training technique"},2,"Neural Networks",1));

        list.add(new Question("Which algorithm is used to update weights in neural networks?",
                new String[]{"Naive Bayes","Gradient Descent","PCA","SVM"},1,"Neural Networks",1));

        list.add(new Question("What is backpropagation used for?",
                new String[]{"Initializing weights","Forward pass","Error correction","Data augmentation"},2,"Neural Networks",2));

        list.add(new Question("A feedforward neural network moves data:",
                new String[]{"In both directions","In a loop","Backward only","Forward only"},3,"Neural Networks",1));

        list.add(new Question("Which network is suitable for sequential data?",
                new String[]{"CNN","RNN","GAN","DNN"},1,"Neural Networks",2));

        list.add(new Question("The vanishing gradient problem mostly affects which network?",
                new String[]{"CNN","GAN","RNN","DNN"},2,"Neural Networks",2));

        list.add(new Question("Which technique helps prevent overfitting in deep networks?",
                new String[]{"Gradient Descent","Dropout","One-hot encoding","Feature Scaling"},1,"Neural Networks",2));

        list.add(new Question("Which function is used for multi-class classification?",
                new String[]{"Sigmoid","Softmax","Tanh","ReLU"},1,"Neural Networks",2));

        list.add(new Question("Which type of RNN can retain longer memory?",
                new String[]{"GRU","LSTM","Vanilla RNN","BiRNN"},1,"Neural Networks",3));
        savequestions(list);
        return list;
    }
}

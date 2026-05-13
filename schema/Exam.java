package schema;

import java.io.Serializable;
import java.util.ArrayList;

public class Exam implements Serializable, CsvExportable {
    private String id;
    private String teacherId;
    private String title;
    private ArrayList<String> questions;
    private ArrayList<String> answers;

    public Exam(String id, String teacherId, String title) {
        this.id = id;
        this.teacherId = teacherId;
        this.title = title;
        this.questions = new ArrayList<>();
        this.answers = new ArrayList<>();
    }

    public void addQuestion(String question, String answer) {
        // add question and answer to the lists
        questions.add(question);
        answers.add(answer);
    }

    public String getId() {
        return id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    @Override
    public String toCsv() {
        // save exam basic info
        String res = id + "," + teacherId + "," + title + ",";
        // loop to save questions and answers
        for (int i = 0; i < questions.size(); i++) {
            res += questions.get(i) + "_" + answers.get(i);
            if (i < questions.size() - 1) {
                res += "#";
            }
        }
        return res;
    }

    public static Exam fromCsv(String csv) {
        // split by comma to max 4 parts
        String[] parts = csv.split(",", 4);
        if (parts.length < 3)
            return null;

        Exam exam = new Exam(parts[0], parts[1], parts[2]);
        // parse the questions and answers part
        if (parts.length == 4 && !parts[3].isEmpty()) {
            String[] qaPairs = parts[3].split("#");
            for (int i = 0; i < qaPairs.length; i++) {
                if (!qaPairs[i].isEmpty()) {
                    String[] qAndA = qaPairs[i].split("_");
                    if (qAndA.length == 2) {
                        exam.addQuestion(qAndA[0], qAndA[1]);
                    }
                }
            }
        }
        return exam;
    }
}

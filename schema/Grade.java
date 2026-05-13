package schema;

import java.io.Serializable;

public class Grade implements Serializable, CsvExportable {
    private String studentId;
    private String examId;
    private double score;
    private double maxScore;
    private String type; // "EXAM" or "CLASSWORK"

    public Grade(String studentId, String examId, double score, double maxScore, String type) {
        this.studentId = studentId;
        this.examId = examId;
        this.score = score;
        this.maxScore = maxScore;
        // check if the type is valid
        if (type != null && (type.equals("EXAM") || type.equals("CLASSWORK"))) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Invalid grade type: " + type);
        }
    }

    public String getStudentId() {
        return studentId;
    }

    public String getExamId() {
        return examId;
    }

    public double getScore() {
        return score;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toCsv() {
        // return the grade as a comma separated string
        return studentId + "," + examId + "," + score + "," + maxScore + "," + type;
    }

    public static Grade fromCsv(String csv) {
        // split the string by commas
        String[] parts = csv.split(",");
        if (parts.length != 5)
            return null;
        try {
            // parse string parts into double values
            return new Grade(parts[0], parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), parts[4]);
        } catch (Exception e) {
            // if numbers are wrong, return null
            return null;
        }
    }
}

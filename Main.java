import java.util.ArrayList;
import java.util.Scanner;

// interface for score
interface Score {
	int getPoints();
}

// abstract class for all questions
abstract class Question implements Score {
	private String questionText;
	private String topic;
	private int difficulty;
	private int points;

	// constructor
	public Question(String questionText, String topic, int difficulty, int points) {
		this.questionText = questionText;
		this.topic = topic;
		this.difficulty = difficulty;
		this.points = points;
	}

	// getters
	public String getQuestionText() {
		return questionText;
	}
	public String getTopic() {
		return topic;
	}
	public int getDifficulty() {
		return difficulty;
	}
	@Override
	public int getPoints() {
		return points;
	}

	// abstract methods - must be in child
	public abstract void displayQuestion();
	public abstract boolean checkAnswer(String answer);
	public abstract String getTip(); // for tip when wrong
}

// MCQ question class
class MCQQuestion extends Question {
	private String[] options;
	private String correctAnswer;
	private String tip; // tip for this question

	public MCQQuestion(String questionText, String topic, int difficulty, int points, String[] options, String correctAnswer, String tip) {
		super(questionText, topic, difficulty, points);
		this.options = options;
		this.correctAnswer = correctAnswer;
		this.tip = tip;
	}

	@Override
	public void displayQuestion() {
		System.out.println(getQuestionText());
		for (int i = 0; i < options.length; i++) {
			System.out.println((i + 1) + ") " + options[i]);
		}
	}

	@Override
	public boolean checkAnswer(String answer) {
		return correctAnswer.equals(answer.trim());
	}

	@Override
	public String getTip() {
		return tip;
	}
}

// True False question class
class TrueFalseQuestion extends Question {
	private String correctAnswer;
	private String tip;

	public TrueFalseQuestion(String questionText, String topic, int difficulty, int points, String correctAnswer, String tip) {
		super(questionText, topic, difficulty, points);
		this.correctAnswer = correctAnswer.toLowerCase();
		this.tip = tip;
	}

	@Override
	public void displayQuestion() {
		System.out.println(getQuestionText());
		System.out.println("1) True");
		System.out.println("2) False");
	}

	@Override
	public boolean checkAnswer(String answer) {
		String userAnswer = answer.trim().toLowerCase();
		// user can write 1 or true
		if (userAnswer.equals("1")) userAnswer = "true";
		else if (userAnswer.equals("2")) userAnswer = "false";
		return correctAnswer.equals(userAnswer);
	}

	@Override
	public String getTip() {
		return tip;
	}
}

// Text question class
class TextQuestion extends Question {
	private String correctAnswer;
	private String tip;

	public TextQuestion(String questionText, String topic, int difficulty, int points, String correctAnswer, String tip) {
		super(questionText, topic, difficulty, points);
		this.correctAnswer = correctAnswer.toLowerCase();
		this.tip = tip;
	}

	@Override
	public void displayQuestion() {
		System.out.println(getQuestionText());
	}

	@Override
	public boolean checkAnswer(String answer) {
		// check with lower case
		return correctAnswer.equals(answer.trim().toLowerCase());
	}

	@Override
	public String getTip() {
		return tip;
	}
}

// class to save all questions
class QuestionBank {
	private ArrayList<Question> questions;
	public QuestionBank() {
		questions = new ArrayList<>();
	}
	public void addQuestion(Question question) {
		questions.add(question);
	}
	public Question getQuestion(int index) {
		return questions.get(index);
	}
	public int getNumberOfQuestions() {
		return questions.size();
	}
}

// class for result
class Result {
	private String name;
	private int totalQuestions;
	private int userScore;
	private int maxScore;
	private double percentage;

	public Result(String name, int totalQuestions, int userScore, int maxScore) {
		this.name = name;
		this.totalQuestions = totalQuestions;
		this.userScore = userScore;
		this.maxScore = maxScore;
		this.percentage = ((double) userScore / maxScore) * 100;
	}

	public void displayResult() {
		System.out.println("\n================================");
		System.out.println(" INTERVIEW RESULT");
		System.out.println("================================");
		System.out.println("Name : " + name);
		System.out.println("Your Score: " + userScore + " / " + maxScore);
		System.out.printf("Percentage: %.2f%%\n", percentage);
		if (percentage >= 90) System.out.println("Level: EXCELLENT");
		else if (percentage >= 70) System.out.println("Level: VERY GOOD");
		else if (percentage >= 50) System.out.println("Level: GOOD");
		else System.out.println("Level: NEEDS IMPROVEMENT");
		System.out.println("================================");
	}
}

// main interview logic
class Interview {
	private QuestionBank questionBank;
	private String name;
	private int maxScore = 0;
	private Scanner input = new Scanner(System.in);
	private ArrayList<Question> wrongQuestions = new ArrayList<>(); // list for wrong answers

	public Interview(QuestionBank questionBank, String name) {
		this.questionBank = questionBank;
		this.name = name;
		// calculate max score
		for (int i = 0; i < questionBank.getNumberOfQuestions(); i++) {
			maxScore = maxScore + questionBank.getQuestion(i).getPoints();
		}
	}

	public void startInterview() {
		int userScore = 0;
		System.out.println("\nWelcome: " + name);

		// first try - ask all questions
		for (int i = 0; i < questionBank.getNumberOfQuestions(); i++) {
			Question q = questionBank.getQuestion(i);
			System.out.println("\nQuestion " + (i + 1) + " | Topic: " + q.getTopic());
			q.displayQuestion();
			System.out.print("Your answer: ");
			String answer = input.nextLine();

			if (q.checkAnswer(answer)) {
				System.out.println("Correct!");
				userScore = userScore + q.getPoints();
			} else {
				System.out.println("Wrong.");
				wrongQuestions.add(q); // save wrong question
			}
		}

		// second try - retry wrong questions with tip
		if (wrongQuestions.size() > 0) {
			System.out.println("\n================================");
			System.out.println(" You have " + wrongQuestions.size() + " wrong answers. Let's try again!");
			System.out.println("================================");

			for (int i = 0; i < wrongQuestions.size(); i++) {
				Question q = wrongQuestions.get(i);
				System.out.println("\nRetry: " + q.getQuestionText());
				System.out.println("Tip: " + q.getTip()); // show tips
				q.displayQuestion();
				System.out.print("Try again: ");
				String answer = input.nextLine();
				if (q.checkAnswer(answer)) {
					System.out.println("Good! Now it's correct. You learned it.");
				} else {
					System.out.println("Still wrong. Remember: " + q.getTip());
				}
			}
		}

		Result result = new Result(name, questionBank.getNumberOfQuestions(), userScore, maxScore);
		result.displayResult();
	}
}

public class Main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter your name: ");
		String name = input.nextLine();

		QuestionBank bank = new QuestionBank();

		// add questions with tips
		String[] oopOptions = {"Java", "HTML", "CSS", "SQL"};
		bank.addQuestion(new MCQQuestion("Which language supports OOP?", "OOP", 1, 10, oopOptions, "1", "OOP means Object Oriented. HTML is not programming language"));

		String[] accessOptions = { "public", "static", "private", "final"};
		bank.addQuestion(new MCQQuestion("Which modifier is most restricted?", "Encapsulation", 1, 10, accessOptions, "3", "private can only be seen inside same class"));

		bank.addQuestion(new TrueFalseQuestion("Inheritance allows a class to reuse another class.", "Inheritance", 2, 20, "true", "Inheritance uses extends keyword"));
		bank.addQuestion(new TrueFalseQuestion("Java does not support polymorphism.", "Polymorphism", 2, 20, "false", "Java supports polymorphism with overloading and overriding"));

		bank.addQuestion(new TextQuestion("What keyword is used to create an interface in Java?", "Interface", 1, 30, "interface", "interface keyword is used to create interface"));
		bank.addQuestion(new TextQuestion("Which keyword is used to hide data?", "Encapsulation", 1, 30, "private", "We hide data to protect it"));

		Interview session = new Interview(bank, name);
		session.startInterview();
	}
}

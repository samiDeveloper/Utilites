package bs.scalaimpatient;

import java.io.IOException;

public class Chapter15Annotations {
	public static void main(String[] args) {

		// 4. sum varargs
		// System.out.println(Chapter15AnnotationsExercises.sum(1, 2, 3)); //
		// does not compile, @varargs seems to have no effect

		// 5. read file
		String filename = Chapter15Annotations.class.getResource("/chapter15AnnotationsExercises.txt").getFile();
		try {
			
			System.out.println(Chapter15AnnotationsExercises.readResource("X" + filename));
			
		} catch(IOException e){
			System.out.println(e);
		}
	}
}

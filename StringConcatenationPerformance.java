/**
 * 
 * @author ucguy4u
 *
 */
public class StringConcatenationPerformance {
	private static final int ITERATION_LIMIT1 = 1;
	private static final int ITERATION_LIMIT2 = 10000000;

	public static void main(String[] args) {
		String s1 = "STRING1-1111111111111111111111";
		String s2 = "STRING2-2222222222222222222222";
		String methodName;
		long startNanos, durationNanos;
		int iteration2;

		System.out.println("ITERATION_LIMIT1: " + ITERATION_LIMIT1);
		System.out.println("ITERATION_LIMIT2: " + ITERATION_LIMIT2);
		System.out.println("s1: " + s1);
		System.out.println("s2: " + s2);
		int iteration1 = 0;
		while (iteration1++ < ITERATION_LIMIT1) {
			System.out.println();
			System.out.println("iteration: " + iteration1);

			// method #0
			methodName = "null";
			iteration2 = 0;
			startNanos = System.nanoTime();
			while (iteration2++ < ITERATION_LIMIT2) {
				method0(s1, s2);
			}
			durationNanos = System.nanoTime() - startNanos;
			System.out.println(
					String.format("%50s: %6.1f nanos", methodName, ((double) durationNanos) / ITERATION_LIMIT2));

			// method #1
			methodName = "s1.concat(s2)";
			iteration2 = 0;
			startNanos = System.nanoTime();
			while (iteration2++ < ITERATION_LIMIT2) {
				method1(s1, s2);
			}
			durationNanos = System.nanoTime() - startNanos;
			System.out.println(
					String.format("%50s: %6.1f nanos", methodName, ((double) durationNanos) / ITERATION_LIMIT2));

			// method #2
			iteration2 = 0;
			startNanos = System.nanoTime();
			methodName = "s1 + s2";
			while (iteration2++ < ITERATION_LIMIT2) {
				method2(s1, s2);
			}
			durationNanos = System.nanoTime() - startNanos;
			System.out.println(
					String.format("%50s: %6.1f nanos", methodName, ((double) durationNanos) / ITERATION_LIMIT2));

			// method #3
			iteration2 = 0;
			startNanos = System.nanoTime();
			methodName = "new StringBuilder(s1).append(s2).toString()";
			while (iteration2++ < ITERATION_LIMIT2) {
				method3(s1, s2);
			}
			durationNanos = System.nanoTime() - startNanos;
			System.out.println(
					String.format("%50s: %6.1f nanos", methodName, ((double) durationNanos) / ITERATION_LIMIT2));

			// method #4
			iteration2 = 0;
			startNanos = System.nanoTime();
			methodName = "new StringBuffer(s1).append(s2).toString()";
			while (iteration2++ < ITERATION_LIMIT2) {
				method4(s1, s2);
			}
			durationNanos = System.nanoTime() - startNanos;
			System.out.println(
					String.format("%50s: %6.1f nanos", methodName, ((double) durationNanos) / ITERATION_LIMIT2));

			// method #5
			iteration2 = 0;
			startNanos = System.nanoTime();
			methodName = "String.format(\"%s%s\", s1, s2)";
			while (iteration2++ < ITERATION_LIMIT2) {
				method5(s1, s2);
			}
			durationNanos = System.nanoTime() - startNanos;
			System.out.println(
					String.format("%50s: %6.1f nanos", methodName, ((double) durationNanos) / ITERATION_LIMIT2));

		}
		System.out.println();
		System.out.println("Tests complete");

	}

	public static String method0(String s1, String s2) {
		return "";
	}

	public static String method1(String s1, String s2) {
		return s1.concat(s2);
	}

	public static String method2(String s1, String s2) {
		return s1 + s2;
	}

	public static String method3(String s1, String s2) {
		return new StringBuilder(s1).append(s2).toString();
	}

	public static String method4(String s1, String s2) {
		return new StringBuffer(s1).append(s2).toString();
	}

	public static String method5(String s1, String s2) {
		return String.format("%s%s", s1, s2);
	}

}
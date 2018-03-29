import static org.junit.Assert.*;

import org.junit.Test;

public class FrameworkTest {

	@Test
	public void test() {
		String[] args1 = new String[] {"testcases/SampleTest_4.txt", "testcases/SampleOutput_5.txt"};
		Framework.main(args1);
	}

}

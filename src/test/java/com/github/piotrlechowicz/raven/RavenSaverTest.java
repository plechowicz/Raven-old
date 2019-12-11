package com.github.piotrlechowicz.raven;

import com.github.piotrlechowicz.raven.annotations.Savable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

/**
 * @author plechowicz
 * created on 12/9/2019.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(RavenSaver.class)
public class RavenSaverTest {

	private final RavenSaver<ClassToSave> ravenSaver = PowerMockito.spy(new RavenSaver<>(ClassToSave.class));

	private List<ClassToSave> rowsToSave;
	private String resultString;
	private String expectedString;

	@Before
	public void setUp() throws Exception {
		Mockito.doAnswer(
				invocation -> {
					List<ClassToSave> rows = invocation.getArgument(2);
					Boolean addHeader = invocation.getArgument(3);
					String result = ravenSaver.getValueAsString(rows, addHeader);
					if (resultString != null) resultString += "\n" + result;
					else resultString = result;
					return resultString;
				}
		).when(ravenSaver).save(anyString(), anyBoolean(), anyList(), anyBoolean());
	}

	public void givenSingleRowToSave() {
		ClassToSave cs = new ClassToSave();
		cs.name = "value";
		cs.name2 = "value2";
		cs.intValue = 0;
		rowsToSave = Collections.singletonList(cs);
		expectedString = ClassToSave.HEADER_NAMES + "\nvalue;value2;0;";
	}

	public void givenTwoRowsToSave() {
		ClassToSave cs = new ClassToSave();
		cs.name = "value";
		cs.name2 = "value2";
		cs.intValue = 0;
		ClassToSave cs2 = new ClassToSave();
		cs2.name = "value3";
		cs2.name2 = "value4";
		cs2.intValue = 1;
		rowsToSave = Arrays.asList(cs, cs2);
		expectedString = ClassToSave.HEADER_NAMES + "\nvalue;value2;0;\nvalue3;value4;1;";
	}


	@Test
	public void canCreateSingleRow() throws IOException {
		givenSingleRowToSave();
		whenSavingWithHeader();
		thenResultValueIsCreatedCorrectly();
	}

	@Test
	public void canCreateTwoRows() throws IOException {
		givenTwoRowsToSave();
		whenSavingWithHeader();
		thenResultValueIsCreatedCorrectly();
	}

	@Test
	public void canCreateMultipleRowsWithAppending() throws IOException {
		givenTwoRowsToSave();
		whenAppendingToResult();
		thenResultValueIsCreatedCorrectly();
	}

	private void thenResultValueIsCreatedCorrectly() {
		Assert.assertEquals(expectedString, resultString);
	}

	private void whenSavingWithHeader() throws IOException {
		ravenSaver.save("", false, rowsToSave, true);
	}

	private void whenAppendingToResult() throws IOException {
		ravenSaver.save("", false, Collections.singletonList(rowsToSave.get(0)), true);
		ravenSaver.save("", true, rowsToSave.subList(1, rowsToSave.size()), false);
	}

	public static class ClassToSave {
		public static String HEADER_NAMES = "name;changeName;intValue;";

		@Savable
		String name;

		@Savable(header = "changeName")
		String name2;

		@Savable(valueFormat = "%d")
		Integer intValue;
	}
}
